package com.example.shuddhavayu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Im
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.MimeTypeFilter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.shuddhavayu.daos.UserDao
import com.example.shuddhavayu.models.Users
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.jar.Attributes.Name


private const val GALLERY_REQUEST_CODE = 105
class MainActivity : AppCompatActivity(), LocationListener {

    private val CAM_PERM_CODE = 101
    private val CAM_REQ_CODE = 102
    lateinit var currentPhotoPath: String
    private lateinit var locationManager: LocationManager
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var address: String
    lateinit var input: String


    val REQUEST_CODE = 100;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        replaceFragment(Home())
        findViewById<BottomNavigationView>(R.id.NavBar).setOnItemSelectedListener {
            when(it.itemId){

                R.id.home -> replaceFragment(Home())
                R.id.report -> replaceFragment(Report())
                R.id.profile -> replaceFragment(Profile())

                else -> {

                }
            }
            true
        }

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTrasaction = fragmentManager.beginTransaction()
        fragmentTrasaction.replace(R.id.constraint_layout, fragment)
        fragmentTrasaction.commit()
    }

    fun openMap(view: View) {
        Toast.makeText(this, "MAP", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }
    fun tapCamera(view: View) {
        askCameraPermission()
    }

    private fun askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CAMERA), CAM_PERM_CODE)
        }else{
//            openCamera()
            dispatchTakePictureIntent()
        }
    }

    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera, CAM_REQ_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAM_PERM_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openCamera()
                dispatchTakePictureIntent()
            }else{
                Toast.makeText(this, "Camera Permission is Required to Use Camera", Toast.LENGTH_SHORT).show()
            }
        }

//        if(requestCode == REQUEST_CODE){
//            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getLocation()
//            }else{
//                Toast.makeText(this, "Location Permission is Required", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAM_REQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                val f = File(currentPhotoPath)
                val intent = Intent(this, PhotoReportActivity::class.java)
                val contentUri = Uri.fromFile(f)
                Log.d("ImageValue", contentUri.toString())
                intent.data = contentUri
                intent.putExtra("fileName", f.name)
                intent.putExtra("latitude", latitude.toString())
                intent.putExtra("longitude",longitude.toString())
                intent.putExtra("address", address)
                startActivity(intent)

                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                    mediaScanIntent.data = Uri.fromFile(f)
                    sendBroadcast(mediaScanIntent)
                }
            }

        }

        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                val contentUri = data?.data
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG $timeStamp.${getFileExt(contentUri)}"
                Log.d("tag", "onActivityResult: Gallery Image Uri: $imageFileName")
                val intent = Intent(this, PhotoReportActivity::class.java)
                intent.data = contentUri
                intent.putExtra("fileName", imageFileName)
                intent.putExtra("latitude", latitude.toString())
                intent.putExtra("longitude",longitude.toString())
                intent.putExtra("address", address)
                startActivity(intent)

            }

        }

    }

    private fun getFileExt(contentUri: Uri?): String {
        val c = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentUri?.let { c.getType(it) }).toString()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAM_REQ_CODE)
                }
            }
        }
    }

    fun tapGallery(view: View) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, GALLERY_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        try{
            locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5f, this)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onLocationChanged(location: Location) {
        try{
            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            address = addresses?.get(0)?.getAddressLine(0).toString()
            latitude = location.latitude
            longitude = location.longitude
            val city = addresses?.get(0)?.locality
            val country = addresses?.get(0)?.countryName
            if(input == "1"){
                findViewById<TextView>(R.id.locationText).text = "$city, $country"
            }else if(input == "2"){
                findViewById<TextView>(R.id.currentLocation).text = address
            }



        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun tapLocation(view: View) {
        val button = view as Button
        if(button.tag.toString() == "1"){
            input = "1"
            getLocation()
            findViewById<TextView>(R.id.locationText).setText("Waiting...")
        }else{
            input = "2"
            getLocation()
            findViewById<TextView>(R.id.currentLocation).setText("Waiting...")
        }

    }

    fun backClick(view: View) {
        replaceFragment(Home())
    }

    fun backCLick(view: View) {
        replaceFragment(Home())
    }

    fun tapAQI(view: View) {
        val intent = Intent(this, AQIActivity::class.java)
        startActivity(intent)
    }
    fun tapWQI(view: View) {
        val intent = Intent(this, AQIActivity::class.java)
        startActivity(intent)
    }


}