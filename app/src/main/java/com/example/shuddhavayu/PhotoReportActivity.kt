package com.example.shuddhavayu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shuddhavayu.daos.UserReportsDao
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firestore.v1.TargetOrBuilder
import org.checkerframework.checker.nullness.qual.NonNull
import org.w3c.dom.Text
import java.util.Locale

class PhotoReportActivity : AppCompatActivity() {
    lateinit var storageReference: StorageReference
    lateinit var latitude: String
    lateinit var longitude: String
    private lateinit var address: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_report)

        val imagePath = intent.data
        val fileName = intent.extras?.getString("fileName")
        latitude = intent.extras?.getString("latitude").toString()
        longitude = intent.extras?.getString("longitude").toString()
        address = intent.extras?.getString("address").toString()
        findViewById<ImageView>(R.id.displayImage).setImageURI(imagePath)

        Log.d("LOC",  "Address $address latitude $latitude longitude $longitude")

        storageReference = FirebaseStorage.getInstance().reference

        findViewById<AppCompatButton>(R.id.submitButton).setOnClickListener{

            uploadImageToFirebase(fileName, imagePath)
            findViewById<AppCompatButton>(R.id.submitButton).visibility = View.GONE
            findViewById<ProgressBar>(R.id.submitProgressBar).visibility = View.VISIBLE
        }

    }


    private fun uploadImageToFirebase(name: String?, contentUri: Uri?) {
        val imageRef = storageReference.child("Report/$name")
        if (contentUri != null) {
            imageRef.putFile(contentUri).addOnSuccessListener { taskSnapshot->

                        imageRef.downloadUrl.addOnSuccessListener {uri->
                            Log.d("tag", "onSuccess: Uploaded Image URI is ${uri.toString()}")
                            val image = uri.toString()
                            val db = FirebaseFirestore.getInstance()
                            var id: Int = 1
                            val docRef = db.collection("UserReport").get()
                            val text = findViewById<EditText>(R.id.descriptionText).text.toString()

                            val userReportDao = UserReportsDao()

                            Log.d("LOC", "image $image Address $address latitude $latitude longitude $longitude")
                            userReportDao.addReport(id, image, latitude, longitude, text, address)

                        }


                Toast.makeText(this, "Image Uploaded:", Toast.LENGTH_SHORT).show()
                Log.d("LOC", "address $address $latitude $longitude")
                findViewById<ImageView>(R.id.successCheck).visibility = View.VISIBLE
                findViewById<ProgressBar>(R.id.submitProgressBar).visibility = View.GONE

            }.addOnFailureListener{
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                findViewById<AppCompatButton>(R.id.submitButton).visibility = View.VISIBLE
                findViewById<ProgressBar>(R.id.submitProgressBar).visibility = View.GONE

            }
        }
    }

    fun goBack(view: View) {
        val intent = Intent(this, MainActivity:: class.java)
        startActivity(intent)
        finish()
    }


}