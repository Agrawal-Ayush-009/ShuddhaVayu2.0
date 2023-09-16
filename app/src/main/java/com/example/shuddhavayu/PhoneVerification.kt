package com.example.shuddhavayu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneVerification : AppCompatActivity() {
    //  private lateinit var binding: ActivityOtpverificationBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)
        //    binding =ActivityOtpverificationBinding.inflate(layoutInflater)
        //   setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()

    }

    fun otpVerify(view: View) {
        val phoneNumber=findViewById<EditText>(R.id.emailId)
        if(phoneNumber.text.toString().isNotEmpty()){
            if((phoneNumber.text.toString()).length ==13){
                Toast.makeText(this,"OTP sent",Toast.LENGTH_SHORT).show()
                otpSend()
                val intent= Intent(this,OTPVerification::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Please enter correct number",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }

    }

    private fun otpSend() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@PhoneVerification, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                val intent=Intent(this@PhoneVerification,OTPVerification::class.java)
                intent.putExtra("VerificationId",verificationId)
                startActivity(intent)
            }
        }
        val phoneNumber=findViewById<EditText>(R.id.emailId)
        val number = "+91" + phoneNumber.text.toString().trim()

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}