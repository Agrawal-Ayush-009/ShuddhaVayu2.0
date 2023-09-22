package com.example.shuddhavayu

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shuddhavayu.daos.UserDao
import com.example.shuddhavayu.models.Users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var auth : FirebaseAuth
    private val TAG = "SignInActivity Tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           // .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        findViewById<Button>(R.id.loginGoogleButton).setOnClickListener{
            signIn()
        }
    }



    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null){
            // Adding User using UserDao
            val map = HashMap<String, Double>()
            map["CO"] = 2.5
            map["NO2"] = 1.8
            map["O3"] = 1.03
            map["SO2"] = 0.7
            map["PM2.5"] = 8.6
            map["PM10"] = 15.2
            map["humidity"] = 60.0
            map["overall_aqi"] = 45.0
            map["overall_wqi"] = 65.0
            map["temperature"] = 25.5
            val userType = "business"
            val deviceID = ""
            val user = Users(firebaseUser.uid, firebaseUser.displayName.toString(), firebaseUser.photoUrl.toString(), map, deviceID, userType)
            val userDao = UserDao()
            userDao.addUser(user)

            val intent = Intent(this, CitizenForm :: class.java)
            startActivity(intent)
            finish()
        }else{
            findViewById<Button>(R.id.loginGoogleButton).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.googleImg).visibility = View.VISIBLE
            findViewById<ProgressBar>(R.id.googleProgressBar).visibility = View.GONE
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account : GoogleSignInAccount = completedTask.getResult(ApiException :: class.java)!!

            Log.d(TAG,"FireBaseAuthWithGoogle: " + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        }catch (e: ApiException){
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "signInResult: failedCode = " + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        findViewById<Button>(R.id.loginGoogleButton).visibility = View.GONE
        findViewById<ImageView>(R.id.googleImg).visibility = View.GONE
        findViewById<ProgressBar>(R.id.googleProgressBar).visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {//Coroutines to do works on thread other than main thread
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {//To Get Back to the main Thread
                updateUI(firebaseUser)
            }
        }

    }

    fun mobileVerify(view: View) {
     //   auth = FirebaseAuth.getInstance()

       // if (auth.currentUser == null) {
            val intent = Intent(this, PhoneVerification::class.java)
            startActivity(intent)
            finish()
       // }

    }
    fun mailVerify(view: View) {
        val intent=Intent(this,MailVerification::class.java)
        startActivity(intent)
    }
}