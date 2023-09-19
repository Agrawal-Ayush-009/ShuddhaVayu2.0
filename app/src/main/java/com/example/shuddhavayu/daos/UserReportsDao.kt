package com.example.shuddhavayu.daos

import com.example.shuddhavayu.models.UserReports
import com.example.shuddhavayu.models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserReportsDao {
    val db = FirebaseFirestore.getInstance()
    val collection = db.collection("UserReports")
    val auth = Firebase.auth

    fun addReport(id:Int, image: String, latitude: String, longitude: String, text: String, address: String){
        GlobalScope.launch(Dispatchers.IO){
            val currUser = auth.currentUser!!.uid

            val user = UserDao().getUserByID(currUser).await().toObject(Users:: class.java)!!

            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            val report = UserReports(id, image, latitude, longitude, user, text, id, address, currentDate, currentTime)

            collection.document().set(report)
        }

    }
}