package com.myisolutions.quran.utils

import okhttp3.internal.immutableListOf

object Constants {
    const val KEY_SCRIPT = "SCRIPT"
    const val KEY_SCRIPT_FONT = "SCRIPT_FONT"
    const val KEY_TRANSLATION = "TRANSLATION"
    const val KEY_TRANSLATION_FONT = "TRANSLATION_FONT"
    const val KEY_ZOOM = "ZOOM"

    var CURRENT_SCRIPT = ""
    var CURRENT_SCRIPT_FONT = ""
    var CURRENT_TRANSLATION = ""
    var CURRENT_TRANSLATION_FONT = ""
    var CURRENT_ZOOM = 0

    val SCRIPTS = mapOf(
        "indopak" to "بِسۡمِ اللهِ الرَّحۡمٰنِ الرَّحِيۡم",
        "uthmani" to "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيم"
    )
    val TRANSLATIONS = mapOf(
        "en" to "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
        "ur" to "اللہ کے نام سےشروع جو نہایت مہربان ہمیشہ رحم فرمانےوالا ہے"
    )
    val AR_FONTS = mapOf(
        "font 1" to "Tajawal",
        "font 2" to "Amiri",
        "font 3" to "Almarai",
        "font 4" to "Lateef",
        "font 5" to "Scheherazade",
        "font 6" to "Harmattan",
        "font 7" to "Mirza",
        "font 8" to "indopak_font"
    )
    val EN_FONTS = mapOf(
        "font 1" to "Ubuntu",
        "font 2" to "Open Sans",
        "font 3" to "Poppins",
        "font 4" to "Merriweather",
        "font 5" to "Lora"
    )
    val UR_FONTS = mapOf(
        "font 1" to "Tajawal",
        "font 2" to "Amiri",
        "font 3" to "Almarai",
        "font 4" to "Lateef",
        "font 5" to "Scheherazade",
        "font 6" to "Harmattan",
        "font 7" to "Mirza",
        "font 8" to "indopak_font"
    )
}