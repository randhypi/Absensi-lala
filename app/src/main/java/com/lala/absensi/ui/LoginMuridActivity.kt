package com.lala.absensi.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lala.absensi.R
import com.lala.absensi.databinding.ActivityLoginMuridBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.bind

class LoginMuridActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMuridBinding
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    private lateinit var nis: String
    private lateinit var password: String

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMuridBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        binding.inputNis.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               nis = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.inputKatasandi.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               password = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.btnMasuk.setOnClickListener {
            login(nis, password)
        }
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

    fun masukKeGuru(view: View) {
        val intent = Intent(this,LoginMuridActivity::class.java)
        startActivity(intent)
    }


}