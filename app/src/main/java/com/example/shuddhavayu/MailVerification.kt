package com.example.shuddhavayu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MailVerification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_verification)
    }

    fun mailOtp(view: View) {
        val mailId=findViewById<EditText>(R.id.emailId)
        if(mailId.text.toString().isNotEmpty()){
            Toast.makeText(this,"OTP sent", Toast.LENGTH_SHORT).show()
            val intent= Intent(this,OTPVerification::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Enter Email Id",Toast.LENGTH_SHORT).show()
        }
    }
}