package com.example.shuddhavayu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.shuddhavayu.models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AQIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aqiactivity)

        val auth = Firebase.auth
        val currentUser = auth.currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Users").document(currentUser)
        var user: Users = Users()
        docRef.get().addOnSuccessListener { docSnapshot->
            user = docSnapshot.toObject(Users::class.java)!!
        }

//        findViewById<TextView>(R.id.aquValue).setText(user!!.device_data["overall_aqi"].toString()).toString()

        val map : HashMap<String, Double> = user!!.device_data!!

//        Toast.makeText(this, "Map is not Null", Toast.LENGTH_SHORT).show()
        map.forEach { (k,v) ->
            Log.d("tag", "$k -> $v")
//            Toast.makeText(this, "$k -> $v", Toast.LENGTH_SHORT).show()
            when(k){
                "CO" -> findViewById<TextView>(R.id.coValue).setText(v.toString()).toString()
                "NO2" -> findViewById<TextView>(R.id.no3Value).setText(v.toString()).toString()
                "O3" -> findViewById<TextView>(R.id.o3Value).setText(v.toString()).toString()
                "PM2.5" -> findViewById<TextView>(R.id.pm2_5Value).setText(v.toString()).toString()
                "PM10" -> findViewById<TextView>(R.id.pm10Value).setText(v.toString()).toString()
                "SO2" -> findViewById<TextView>(R.id.so2Value).setText(v.toString()).toString()
            }
        }
    }


    fun back(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}