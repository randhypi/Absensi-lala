package com.lala.absensi.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.lala.absensi.databinding.ActivityHomeMuridBinding
import org.koin.ext.getOrCreateScope
import java.util.*

class HomeMuridActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeMuridBinding
    private val REQUET_CODE_LOCATION_PERMISSION: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMuridBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (ContextCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                listOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray(),
                REQUET_CODE_LOCATION_PERMISSION
            )
        } else {
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUET_CODE_LOCATION_PERMISSION && grantResults.size > 0) {
            if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getCurrentLocation() {

        var address: MutableList<Address>

        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 100
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {

                if (locationResult.locations.size > 0) {
                    val lastLocation: Int = locationResult.locations.size - 1

                    val latitude: Double = locationResult.locations.get(lastLocation).latitude
                    val longitude: Double = locationResult.locations.get(lastLocation).longitude

                    val geocoder = Geocoder(applicationContext, Locale.getDefault())
                    try {
                        address = geocoder.getFromLocation(latitude, longitude, 1)

                        val fullAddress = address[0].getAddressLine(0)

                        val provinsi = address[0].adminArea.toString()
                        val kota = address[0].subAdminArea.toString()
                        val kecamatan = address[0].locality.toString()

                        binding.tvAlamat.text = fullAddress


                        Log.d("LATITUDE", latitude.toString())
                        Log.d("LONGITUDE", longitude.toString())
                        Log.d("ALAMAT", fullAddress.toString())
                    } catch (e: Exception) {

                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
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

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())


        LocationServices.getFusedLocationProviderClient(applicationContext)
            .removeLocationUpdates(locationCallback)

    }
}