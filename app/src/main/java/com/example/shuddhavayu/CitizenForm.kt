package com.example.shuddhavayu

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shuddhavayu.daos.FormDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class CitizenForm : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var edtTxtDate: EditText
    lateinit var formDao: FormDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citizen_form)

        auth = Firebase.auth

        edtTxtDate = findViewById<EditText>(R.id.DOB)

        edtTxtDate.showSoftInputOnFocus = false

        edtTxtDate.setOnClickListener{
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,

                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    edtTxtDate.setText(dat)
                },

                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        val submit = findViewById<Button>(R.id.submitButton)

        submit.setOnClickListener{
            val name = findViewById<EditText>(R.id.Name).text.toString()
            val dob = findViewById<EditText>(R.id.DOB).text.toString()
            val location = findViewById<EditText>(R.id.location).text.toString()
            val email = findViewById<EditText>(R.id.Email).text.toString()

            if(name.isEmpty() || dob.isEmpty() || location.isEmpty() || email.isEmpty()){
                Toast.makeText(this, "Fill all the Fields!", Toast.LENGTH_SHORT).show()
            }else{
                formDao = FormDao()

                formDao.addForm(name, dob, location, email)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}