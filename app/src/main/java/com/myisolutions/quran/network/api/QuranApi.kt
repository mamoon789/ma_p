package com.myisolutions.quran.network.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.myisolutions.quran.network.models.Quran
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "http://api.alquran.cloud/v1/quran/"//"https://ia801003.us.archive.org/"

interface QuranApi {
    @GET("quran-simple")
    suspend fun getQuran(): Quran

    @GET("BookReader/BookReaderImages.php?zip=/30/items/quranmadinanomargins/Quran_Madina_Nomargins_jp2.zip&id=quranmadinanomargins&scale=4&rotate=0")
    suspend fun getQuranImage(@Query("file")file: String): ResponseBody

    companion object {
        operator fun invoke(): QuranApi {
            val quranApi: QuranApi by lazy {
                Retrofit.Builder()
                    .client(OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .baseUrl(BASE_URL)
                    .build()
                    .create(QuranApi::class.java)
            }
            return quranApi
        }
    }
}