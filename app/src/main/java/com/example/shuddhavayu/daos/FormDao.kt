package com.example.shuddhavayu.daos

import com.example.shuddhavayu.models.Form
import com.example.shuddhavayu.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FormDao {
    val db = FirebaseFirestore.getInstance()
    val formCollection = db.collection("Forms")
    val auth = Firebase.auth

    fun addForm(name: String, dob: String, location: String, email: String){
        GlobalScope.launch(Dispatchers.IO){
            val currUser = auth.currentUser!!.uid

            val user = UserDao().getUserByID(currUser).await().toObject(Users:: class.java)!!

            val form = Form(user, name, dob, location, email)

            formCollection.document().set(form)
        }
    }

    private fun getFormByID(formID: String): Task<DocumentSnapshot> {
        return formCollection.document(formID).get()
    }

}