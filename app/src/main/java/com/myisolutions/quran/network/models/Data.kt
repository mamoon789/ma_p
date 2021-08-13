package com.myisolutions.quran.network.models

data class Data(
    val surahs: MutableList<Surah>,
    val edition: Edition
)