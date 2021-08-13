package com.myisolutions.quran.network.models

data class Ayah(
    val hizbQuarter: Int,
    val juz: Int,
    val manzil: Int,
    val number: Int,
    val numberInSurah: Int,
    val page: Int,
    val ruku: Int,
    val sajda: Any,
    var text: String,
    var translation: String,
    var audio: String
)