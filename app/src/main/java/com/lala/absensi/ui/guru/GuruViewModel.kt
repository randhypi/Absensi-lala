package com.lala.absensi.ui.guru

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lala.absensi.model.ModelGuru
import com.lala.absensi.model.ModelKehadiranMurid
import com.lala.absensi.model.ModelMurid
import com.lala.absensi.utils.Date

class GuruViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    private val _dataKehadiran: MutableState<List<ModelKehadiranMurid>> = mutableStateOf(listOf())
    private val _dataKepulangan: MutableState<List<ModelKehadiranMurid>> = mutableStateOf(listOf())

    val dataGuru: MutableState<ModelGuru> = mutableStateOf(ModelGuru())

    val dataKehadiranMurid: MutableState<List<ModelKehadiranMurid>> = mutableStateOf(listOf())
    val dataKepulanganMurid: MutableState<List<ModelKehadiranMurid>> = mutableStateOf(listOf())

    init {
        getDataKehadiranMurid()
        getDataKepulanganMurid()
        getDataGuru()
    }

    fun getDataKehadiranMurid() {

        db.collection("masuk")
            .addSnapshotListener { snapshot, e ->
                val value = snapshot?.toObjects(ModelKehadiranMurid::class.java)
                _dataKehadiran.value = value as List<ModelKehadiranMurid>
                val kehadiran = _dataKehadiran.value.filter {
                    it.hariTanggal == Date().getDate()
                }
                dataKehadiranMurid.value = kehadiran
                Log.d("DATA KEHADIRAN", "${_dataKehadiran.value}")

            }

    }


    fun getDataKepulanganMurid() {

        db.collection("pulang")
            .addSnapshotListener { snapshot, e ->
                val value = snapshot?.toObjects(ModelKehadiranMurid::class.java)
                _dataKepulangan.value = value as List<ModelKehadiranMurid>
                val kehadiran = _dataKepulangan.value.filter {
                    it.hariTanggal == Date().getDate()
                }
                dataKepulanganMurid.value = kehadiran
                Log.d("DATA KEHADIRAN", "${_dataKehadiran.value}")

            }

    }


    fun getDataGuru() {
        db.collection("guru").document(auth.uid.toString())
            .addSnapshotListener { snapshot, e ->
                val value = snapshot?.toObject(ModelGuru::class.java)
                value?.let {
                    dataGuru.value = it
                }
            }
    }

}