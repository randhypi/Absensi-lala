package com.lala.absensi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.lala.absensi.R
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Log.d("SplashScreen","cek ${auth.currentUser}")
        supportActionBar?.hide()


        Timer().schedule(3000L){
            if (auth.currentUser?.email == "" || auth.currentUser?.email == null) {
                Log.d("SplashScreen","if ${auth.currentUser?.email}")
                val intent = Intent(this@SplashScreenActivity, LoginMuridActivity::class.java)
                startActivity(intent)
            } else if (auth.currentUser?.email == "QXr1nNq4qDbAd82KPEyR3Dqpr2r1") {
                Log.d("SplashScreen","if ${auth.currentUser?.email}")
                val intent = Intent(this@SplashScreenActivity, HomeGuruActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("SplashScreen","if ${auth.currentUser?.email}")
                val intent = Intent(this@SplashScreenActivity, HomeMuridActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
