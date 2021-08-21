package com.lala.absensi.utils

import java.util.*
import java.util.Date

class Date {
    fun getDate(): String {

        val now = Calendar.getInstance()
        val day = now.get(Calendar.DAY_OF_WEEK)
        val date = now.get(Calendar.DAY_OF_MONTH)
        val month = now.get(Calendar.MONTH)
        val year = now.get(Calendar.YEAR)
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)

        val dayName = when (day) {
            2 -> "Senin"
            3 -> "Selasa"
            4 -> "Rabu"
            5 -> "Kamis"
            6 -> "Jum'at"
            7 -> "Sabtu"
            1 -> "Minggu"

            else -> "No Day"
        }
        val monthName = when (month) {
            0 -> "Januari"
            1 -> "Februari"
            2 -> "Maret"
            3 -> "April"
            4 -> "Mei"
            5 -> "Juni"
            6 -> "Juli"
            7 -> "Agustus"
            8 -> "September"
            9 -> "Oktober"
            10 -> "November"
            11 -> "Desember"

            else -> "No Month"
        }

        return "$dayName, $date/$monthName/$year, $hour:$minute "
    }
}