package com.example.shuddhavayu

import android.Manifest
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.type.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder.Permission
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

lateinit var supportMapFragment: SupportMapFragment
lateinit var fusedLocationProviderClient: FusedLocationProviderClient

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        Dexter.withContext(applicationContext)
            .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                @RequiresApi(34)
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // Permission granted, handle the logic here
                    getCurrentLocation()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // Permission denied, handle the logic here
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    // Handle permission rationale if needed, and call token.continuePermissionRequest() when done
                    token.continuePermissionRequest()
                }
            }).check()

    }

    @RequiresApi(34)
    private fun getCurrentLocation() {
         if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val task: Task<android.location.Location> = fusedLocationProviderClient.getLastLocation()

        task.addOnSuccessListener {location->
            supportMapFragment.getMapAsync{googleMap->
                if(location != null){
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val latLng = com.google.android.gms.maps.model.LatLng(latitude, longitude)
                    val markerOptions = MarkerOptions().position(latLng).title("Current Location!")
                    googleMap.addMarker(markerOptions)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }else{
                    Toast.makeText(this, "Please Turn on your App Location Permission", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}