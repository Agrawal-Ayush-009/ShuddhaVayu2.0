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
import androidx.appcompat.app.AlertDialog
//import com.example.shuddhavayu.databinding.ActivityOtpverificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


class OTPVerification : AppCompatActivity() {
    private lateinit var verificationId:String
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)
        auth=FirebaseAuth.getInstance()
        val builder=AlertDialog.Builder(this)
        builder.setMessage("Please Wait ...")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog=builder.create()
        dialog.show()

        val phoneNumber=intent.getStringExtra("PhoneNumber").toString().trim()
        val options=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : OnVerificationStateChangedCallbacks(){

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OTPVerification,"Please try again",Toast.LENGTH_SHORT).show()
                    val intent =Intent(this@OTPVerification,PhoneVerification::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    verificationId=p0

                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        cursorMove()

    }

    private fun cursorMove() {
        val input1 = findViewById<EditText>(R.id.otp1)
        val input2 = findViewById<EditText>(R.id.otp2)
        val input3 = findViewById<EditText>(R.id.otp3)
        val input4 = findViewById<EditText>(R.id.otp4)
        val input5 = findViewById<EditText>(R.id.otp5)
        val input6 = findViewById<EditText>(R.id.otp6)

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

        input5.addTextChangedListener(object : TextWatcher {
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
                    input6.requestFocus()
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
        val input6 = findViewById<EditText>(R.id.otp6)

        if (input1.text.toString().trim().isNotEmpty() &&
            input2.text.toString().trim().isNotEmpty() &&
            input3.text.toString().trim().isNotEmpty() &&
            input4.text.toString().trim().isNotEmpty() &&
            input5.text.toString().trim().isNotEmpty() &&
            input6.text.toString().trim().isNotEmpty()
        ) {
           // Toast.makeText(this, "Verifying OTP", Toast.LENGTH_SHORT).show()
            val code = input1.text.toString().trim()+input2.text.toString().trim()+
                    input3.text.toString().trim()+input4.text.toString().trim()+ input5.text.toString().trim()+
                    input6.text.toString().trim()

            dialog.show()
            val credential = PhoneAuthProvider.getCredential(verificationId,code)
            auth.signInWithCredential(credential)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        dialog.dismiss()
                        startActivity(Intent(this@OTPVerification,CitizenForm::class.java))
                    }else{
                        dialog.dismiss()
                        Toast.makeText(this,"Error ${it.exception}",Toast.LENGTH_SHORT).show()
                    }
                }


        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }
    }
}

