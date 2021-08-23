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
    private val _dataMurid: MutableState<List<ModelMurid>> = mutableStateOf(listOf())

    val dataGuru: MutableState<ModelGuru> = mutableStateOf(ModelGuru())
    val dataKehadiranMurid: MutableState<List<ModelMurid>> = mutableStateOf(listOf())

    init {
        getDataKehadirdanMurid()
        getDataGuru()
    }

    fun getDataKehadirdanMurid() {
        val listMurid = arrayListOf<ModelMurid>()
        val listDataMurid = arrayListOf<ModelMurid>()

        db.collection("masuk")
            .addSnapshotListener { snapshot, e ->
                val value = snapshot?.toObjects(ModelKehadiranMurid::class.java)
                _dataKehadiran.value = value as List<ModelKehadiranMurid>
                Log.d("DATA KEHADIRAN", "${_dataKehadiran.value}")
                db.collection("murid").addSnapshotListener { snapshot1, error ->
                    snapshot1?.documentChanges?.forEach {
                        val _listDataMurid = arrayListOf<ModelMurid>()
                        _listDataMurid.add(ModelMurid(
                            idMurid = it.document.get("id").toString(),
                            email = it.document.get("email").toString(),
                            jenisKelamin = it.document.get("jenisKelamin").toString(),
                            jurusan = it.document.get("jurusan").toString(),
                            nama = it.document.get("nama").toString(),
                            nis = it.document.get("nis").toString(),
                        ))
                        listDataMurid.addAll(_listDataMurid)
                    }

                    _dataMurid.value = listDataMurid

                    Log.d("DATA MURID VIEWMODEL", "${_dataMurid.value}")

                    val findWaktu = _dataKehadiran.value.filter { waktu ->
                        waktu.hariTanggal == Date().getDate()

                    }
                    Log.d("DATA FIND WAKTU", "$findWaktu")


                    findWaktu.forEach { waktu ->
                        val filterMurid = _dataMurid.value.filter { murid ->
                            murid.idMurid == waktu.idMurid
                        }

                        Log.d("DATA FIND ID MURID", waktu.idMurid)
                        Log.d("DATA FILTER MURID", "$filterMurid")

                        filterMurid.forEach {
                            val _listMurid = arrayListOf<ModelMurid>()
                            val kehadiranMurid = findWaktu.find { waktu ->
                                waktu.idMurid == waktu.idMurid
                            }

                            kehadiranMurid?.let { waktu ->
                                _listMurid.add(ModelMurid(
                                    idMurid = it.idMurid,
                                    email = it.email,
                                    jenisKelamin = it.jenisKelamin,
                                    jurusan = it.jurusan,
                                    nama = it.nama,
                                    nis = it.nis,
                                    kehadiranMurid = waktu
                                ))
                            }
                            listMurid.addAll(_listMurid)
                        }
                        dataKehadiranMurid.value = listMurid
                        Log.d("DATA MURID1","${dataKehadiranMurid.value}")
                    }
                }
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