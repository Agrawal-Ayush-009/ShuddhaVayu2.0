package com.example.shuddhavayu

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class PhotoReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_report)

//        val extras = intent.extras
//        val byteArray = extras?.getByteArray("picture")
//
//        if (byteArray != null) {
//            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//            val image = findViewById<ImageView>(R.id.displayImage)
//            image.setImageBitmap(bmp)
//        }

        val imagePath = intent.getStringExtra("picture")
        findViewById<ImageView>(R.id.displayImage).setImageURI(Uri.parse(imagePath))
    }

    fun goBack(view: View) {
        val intent = Intent(this, MainActivity:: class.java)
        startActivity(intent)
        finish()
    }
}