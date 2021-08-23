package com.lala.absensi.model

data class ModelKehadiranMurid(
    var idMurid: String = "",
    var lokasi: String = "",
    var hariTanggal: String = "",
    var waktu: String = "",
    val dataMurid: ModelMurid = ModelMurid()
)