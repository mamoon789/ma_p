package com.myisolutions.quran.network.models

class Surah(
    val ayahs: MutableList<Ayah>,
    val englishName: String,
    val englishNameTranslation: String,
    val name: String,
    val number: Int,
    val revelationType: String
) {
    fun getAyahsId(): Array<String> {
        val a: MutableList<String> = ArrayList()
        for (ayah in ayahs) {
            a.add("ayah" + ayah.number)
        }
        return a.toTypedArray()
    }

    fun getAyahsText(): Array<String> {
        val a: MutableList<String> = ArrayList()
        for (ayah in ayahs) {
            a.add(ayah.text)
        }
        return a.toTypedArray()
    }
}