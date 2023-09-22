package com.example.shuddhavayu
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private lateinit var mAuth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var phoneNumberEditText: EditText
    val verificationId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)

        mAuth = FirebaseAuth.getInstance()
        phoneNumberEditText = findViewById(R.id.phoneNumber)

        if (mAuth.currentUser != null) {
            val intent = Intent(this, CitizenForm::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.Button).setOnClickListener {
            phoneNumberEditText=findViewById(R.id.phoneNumber)
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            if (phoneNumber.isNotEmpty()) {
                if (phoneNumber.length == 13) {
                    Toast.makeText(this, "OTP sent", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,OTPVerification::class.java)

                    val d = intent.putExtra("PhoneNumber",phoneNumber)

                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Please enter correct number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
