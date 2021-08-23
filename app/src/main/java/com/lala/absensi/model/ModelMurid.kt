package com.lala.absensi.model

data class ModelMurid(
    val idMurid: String ="",
    var email: String = "",
    var jenisKelamin: String = "",
    var jurusan: String = "",
    var nama: String = "",
    var nis: String = "",
    var kehadiranMurid: ModelKehadiranMurid = ModelKehadiranMurid()
)