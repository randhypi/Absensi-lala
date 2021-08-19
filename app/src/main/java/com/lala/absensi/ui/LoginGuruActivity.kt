package com.lala.absensi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.lala.absensi.R
import com.lala.absensi.databinding.ActivityLoginGuruBinding

class LoginGuruActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()

    private lateinit var binding: ActivityLoginGuruBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_guru)

        supportActionBar?.hide()

    }

    private fun login(email: String,password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                val intent = Intent(this@LoginGuruActivity,HomeGuruActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this,"Kesalahan dalam login",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        System.exit(0)
        super.onBackPressed()
    }
}