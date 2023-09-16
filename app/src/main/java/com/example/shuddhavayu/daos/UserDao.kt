package com.example.shuddhavayu.daos

import com.example.shuddhavayu.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("Users")

    fun addUser(user: Users?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.uID).set(it)
            }
        }
    }
    fun getUserByID(uid: String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }
}