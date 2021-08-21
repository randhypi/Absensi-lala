package com.lala.absensi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.lala.absensi.R
import com.lala.absensi.databinding.ActivityLoginGuruBinding

class LoginGuruActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()


    private lateinit var password: String
    private lateinit var email: String

    private lateinit var binding: ActivityLoginGuruBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginGuruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                email = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputKatasandi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                password = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.btnMasuk.setOnClickListener {
            login(email, password)
        }
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


    fun masukKeMurid(view: View) {
        val intent = Intent(this,LoginMuridActivity::class.java)
        startActivity(intent)
    }
}