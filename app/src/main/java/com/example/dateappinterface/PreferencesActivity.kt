package com.example.dateappinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.google.android.material.slider.Slider
import java.lang.Exception
import java.sql.ResultSet
import java.sql.Statement

class PreferencesActivity : AppCompatActivity() {
    internal lateinit var agePreference: CrystalRangeSeekbar
    internal lateinit var setMinAge: TextView
    internal lateinit var setMaxAge: TextView
    internal lateinit var maleChecked: CheckBox
    internal lateinit var femaleChecked: CheckBox
    internal lateinit var email: String
    var minAge = 0
    var maxAge = 0
    var genderPref = ""
    var activeID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        agePreference = findViewById(R.id.rangeSeekbar)
        setMinAge = findViewById(R.id.setMinAge)
        setMaxAge = findViewById(R.id.setMaxAge)
        maleChecked = findViewById(R.id.checkMale)
        femaleChecked = findViewById(R.id.checkFemale)
        email = intent.getStringExtra("email").toString()

        agePreference.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            minAge = minValue.toInt()
            maxAge = maxValue.toInt()
            setMinAge.text = minValue.toString()
            setMaxAge.text = maxValue.toString()
        }
        var st: Statement
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()

            if(connect != null){
                var sql = "SELECT [UserID] FROM tbl_UserInfo WHERE Email = '$email'"
                var st:Statement = connect.createStatement()
                var rs: ResultSet = st.executeQuery(sql)
                Log.d("msg","Success")
                rs.next()
                activeID = rs.getInt(1)
                Log.d("ACTIVE ID", activeID.toString())
            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            var z = ex.message.toString()
            Log.d("error", z)
        }

    }


    //Save preferences and go to pair search
    fun goToPairSearch(view: View){
        var st: Statement
        var ifMaleChecked = false
        var ifFemaleChecked = false
        if(maleChecked.isChecked){
            ifMaleChecked = true
            genderPref = "Male"
        }
        if(femaleChecked.isChecked){
            ifFemaleChecked = true
            genderPref = "Female"
        }
        if(ifMaleChecked and ifFemaleChecked){
            genderPref = "Both"
        }
        if(!ifMaleChecked and !ifFemaleChecked){
            maleChecked.error = "Należy zaznaczyć conajmniej jedną opcję"
            femaleChecked.error = "Należy zaznaczyć conajmniej jedną opcję"
            return
        }
        //Save Preferences
        try{
            var connectionHelper = ConnectionHelper()
            var connect = connectionHelper.connectionclass()
            if(connect != null){
                var sql = "INSERT INTO tbl_Preferences (userID, minAgePref, maxAgePref, genderPref) VALUES ('$activeID', '$minAge', '$maxAge', '$genderPref')"
                st = connect.createStatement()
                st.execute(sql)
                Log.d("msg","Success")

            }
            else{
                Log.d("sd","CONNECTION PROBLEM")
            }

        }
        catch(ex: Exception){
            var z = ex.message.toString()
            Log.d("error", z)
        }

        val intent = Intent(this, PairSearchActivity::class.java).apply {  }
        startActivity(intent)
    }
}