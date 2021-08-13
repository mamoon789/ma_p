package com.myisolutions.quran.repository

import com.myisolutions.quran.network.api.QuranApi
import com.myisolutions.quran.network.models.Quran
import okhttp3.ResponseBody

class Repository {
    suspend fun getQuran(): Quran = QuranApi().getQuran()

    suspend fun getQuranImage(page: String): ResponseBody =
        QuranApi().getQuranImage("Quran_Madina_Nomargins_jp2/Quran_Madina_Nomargins_" + page + ".jp2")
}