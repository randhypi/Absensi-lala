package com.lala.absensi.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.lala.absensi.R
import com.lala.absensi.ui.guru.HomeGuruActivity
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()

    private val REQUET_CODE_LOCATION_PERMISSION: Int = 1

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Log.d("SplashScreen", "cek ${auth.currentUser}")
        supportActionBar?.hide()

        if (ContextCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                listOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray(),
                REQUET_CODE_LOCATION_PERMISSION
            )
            timer()
        } else {
            timer()
        }


    }

    private fun timer() {
        Timer().schedule(3000L) {
//            val intent = Intent(this@SplashScreenActivity, HomeGuru2Activity::class.java)
//            startActivity(intent)
            if (auth.currentUser?.email == "" || auth.currentUser?.email == null) {
                Log.d("SplashScreen", "if ${auth.currentUser?.email}")
                val intent = Intent(this@SplashScreenActivity, LoginMuridActivity::class.java)
                startActivity(intent)
            } else if (auth.currentUser?.email == "QXr1nNq4qDbAd82KPEyR3Dqpr2r1") {
                Log.d("SplashScreen", "if ${auth.currentUser?.email}")
                val intent = Intent(this@SplashScreenActivity, HomeGuruActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("SplashScreen", "else ${auth.currentUser?.email}")
                val intent = Intent(this@SplashScreenActivity, HomeMuridActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
