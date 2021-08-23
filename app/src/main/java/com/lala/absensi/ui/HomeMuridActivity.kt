package com.lala.absensi.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lala.absensi.databinding.ActivityHomeMuridBinding
import com.lala.absensi.model.ModelKehadiranMurid
import com.lala.absensi.model.ModelMurid
import com.lala.absensi.utils.Date
import kotlinx.coroutines.*
import java.util.*

class HomeMuridActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    private lateinit var binding: ActivityHomeMuridBinding
    private val REQUET_CODE_LOCATION_PERMISSION: Int = 1


    @DelicateCoroutinesApi
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
            getCurrentLocation()
        } else {
            getCurrentLocation()

        }
    }

    @DelicateCoroutinesApi
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


    @DelicateCoroutinesApi
    private fun getCurrentLocation() {

        var address: MutableList<Address>
        var completeAddress = ""

        val locationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 100
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                LocationServices.getFusedLocationProviderClient(this@HomeMuridActivity)
                    .removeLocationUpdates(this)
                if (locationResult.locations.size > 0) {
                    val lastLocation: Int = locationResult.locations.size - 1

                    val latitude: Double = locationResult.locations.get(lastLocation).latitude
                    val longitude: Double = locationResult.locations.get(lastLocation).longitude

                    val geocoder = Geocoder(applicationContext, Locale.getDefault())

                    try {
                        address = geocoder.getFromLocation(latitude, longitude, 1)

                        completeAddress = address[0].getAddressLine(0)
                        binding.tvLokasiResult.text = completeAddress


                        addAttandance()
                        Log.d("LATITUDE", latitude.toString())
                        Log.d("LONGITUDE", longitude.toString())
                        Log.d("ALAMAT", completeAddress.toString())
                    } catch (e: Exception) {
                        Log.d("Exception", e.toString())
                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            true
        }

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())


    }


    private fun getDate(): String {
        val date = Date()
        Log.d("DATE", date.getDate())
        return date.getDate()
    }

    private fun getTime(): String {
        val time = Date()
        return time.getTime()
    }

    @SuppressLint("SetTextI18n")
    private fun addAttandance() {
        db.collection("murid").document(auth.uid.toString())
            .addSnapshotListener { value, error ->
                binding.name.text = value?.get("nama").toString()
                binding.tvNamaResult.text = value?.get("nama").toString()
                binding.tvNis.text = value?.get("nis").toString()
                value?.toObject(ModelMurid::class.java)?.let {
                    db.collection("masuk").document()
                        .set(ModelKehadiranMurid(
                            idMurid = auth.uid.toString(),
                            lokasi = binding.tvLokasiResult.text.toString(),
                            hariTanggal = getDate(),
                            waktuMasuk = getTime(),
                            dataMurid = it
                        )).addOnSuccessListener {
                            binding.tvWaktuResult.text = getDate() + " " + getTime()
                        }.addOnFailureListener {
                            Log.d("LOCATION", it.toString())
                        }
                }


            }


    }


    override fun onBackPressed() {
        Toast.makeText(this, "Kamu tidak bisa menekan kembali", Toast.LENGTH_SHORT).show()
    }


    fun pulang(view: View) {
        val intent = Intent(this, PulangActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }
}