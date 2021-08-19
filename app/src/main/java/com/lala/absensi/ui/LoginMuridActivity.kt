package com.lala.absensi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lala.absensi.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginMuridActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_murid)
        supportActionBar?.hide()


    }

    @DelicateCoroutinesApi
    private fun login(nis: String, password: String) {

        db.collection("murid")
            .addSnapshotListener { value, error ->
                val murid = value?.documents?.find {
                    it.get("nis") == nis
                }
                Log.d("CHECK", murid.toString())
                val dbNis = murid?.get("nis")
                val dbEmail = murid?.get("email").toString().lowercase()
                Log.d("Check nisa", "$dbNis")
                auth.signInWithEmailAndPassword(dbEmail, password)
                    .addOnSuccessListener {
                        val intent = Intent(this@LoginMuridActivity, HomeGuruActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext,
                            "Kesalahan dalam login",
                            Toast.LENGTH_SHORT).show()
                    }
            }


    }

    override fun onBackPressed() {
        System.exit(0)
        super.onBackPressed()
    }
}