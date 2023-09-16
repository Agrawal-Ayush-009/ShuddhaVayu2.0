package com.example.shuddhavayu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import com.example.shuddhavayu.databinding.ActivityOtpverificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


class OTPVerification : AppCompatActivity() {
    private var verificationId:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)
        cursorMove()
        verificationId= intent.getStringExtra("VerificationId").toString()
    }

    private fun cursorMove() {
        val input1 = findViewById<EditText>(R.id.otp1)
        val input2 = findViewById<EditText>(R.id.otp2)
        val input3 = findViewById<EditText>(R.id.otp3)
        val input4 = findViewById<EditText>(R.id.otp4)
        val input5 = findViewById<EditText>(R.id.otp5)

        input1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().trim().isEmpty()) {
                    input2.requestFocus()
                }
            }
        })

        input2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    input3.requestFocus()
                }
            }
        })

        input3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().trim().isEmpty()) {
                    input4.requestFocus()
                }
            }
        })

        input4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().trim().isEmpty()) {
                    input5.requestFocus()
                }
            }
        })

    }

    fun otpButton(view: View) {
        val input1 = findViewById<EditText>(R.id.otp1)
        val input2 = findViewById<EditText>(R.id.otp2)
        val input3 = findViewById<EditText>(R.id.otp3)
        val input4 = findViewById<EditText>(R.id.otp4)
        val input5 = findViewById<EditText>(R.id.otp5)

        if (input1.text.toString().trim().isNotEmpty() &&
            input2.text.toString().trim().isNotEmpty() &&
            input3.text.toString().trim().isNotEmpty() &&
            input4.text.toString().trim().isNotEmpty() &&
            input5.text.toString().trim().isNotEmpty()
        ) {
            Toast.makeText(this, "Verifying OTP", Toast.LENGTH_SHORT).show()
            if(verificationId!=null) {
                val code = input1.text.toString().trim()+input2.text.toString().trim()+
                        input3.text.toString().trim()+input4.text.toString().trim()+ input5.text.toString().trim()

                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (code=="13579") {
                            Toast.makeText(this,"OTP verified",Toast.LENGTH_SHORT).show()
                            val intent= Intent(this@OTPVerification,CitizenForm::class.java)
                            startActivity(intent)

                            finish()
                        } else {
                            Toast.makeText(this@OTPVerification,"Invalid OTP",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }
    }
}

