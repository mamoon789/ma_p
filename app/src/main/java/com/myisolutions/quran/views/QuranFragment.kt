package com.myisolutions.quran.views

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.myisolutions.quran.R
import com.myisolutions.quran.network.models.Quran
import com.myisolutions.quran.network.models.Surah
import com.myisolutions.quran.utils.Constants
import com.myisolutions.quran.utils.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Serializable


@Suppress("UNCHECKED_CAST")
class QuranFragment : Fragment(), MediaPlayer.OnCompletionListener {
    lateinit var btTranslation: Button
    lateinit var btPlayback: Button

    lateinit var surahs: MutableList<Surah>
    lateinit var edition: String

    private lateinit var tts: TextToSpeech
    private lateinit var wv: WebView
    private lateinit var sb: SeekBar

    private val htmlStart = "<html>\n"
    private val headStart = "<head>\n"
    private val style = "<style>\n" +
            "@import url('https://fonts.googleapis.com/css2?family=Ubuntu&family=Open+Sans&family=Poppins&family=Merriweather&family=Lora&display=swap');\n" +
            "@import url('https://fonts.googleapis.com/css2?family=Tajawal&family=Amiri:wght@700&family=Almarai&family=Lateef&family=Scheherazade:wght@700&family=Harmattan&family=Mirza:wght@500&display=swap');\n" +
            "@font-face {\n" +
            "   font-family: 'indopak_font';\n" +
            "   src: url('file:///android_asset/indopak_font.woff');\n" +
            "}\n" +
            "*{\n" +
            "   text-align: justify;\n" +
            "   text-align-last: center;\n" +
            "   scroll-behavior: smooth;\n" +
            "}\n" +
            ".ar {\n" +
            "   font-family: ${Constants.CURRENT_SCRIPT_FONT};\n" +
            "   padding-top: 5px;\n" +
            "   padding-bottom: 10px;\n" +
            "   font-size: 175%;\n" +
            "   direction: rtl;\n" +
            "}\n" +
            ".ur {\n" +
            "   font-family: ${Constants.CURRENT_TRANSLATION_FONT};\n" +
            "   font-size: 150%;\n" +
            "   padding-top: 5px;\n" +
            "   padding-bottom: 10px;\n" +
            "   direction: rtl;\n" +
            "}\n" +
            ".en {\n" +
            "   font-family: ${Constants.CURRENT_TRANSLATION_FONT};\n" +
            "   font-size: 100%;\n" +
            "   padding-top: 5px;\n" +
            "   padding-bottom: 10px;\n" +
            "   direction: ltr;\n" +
            "}\n" +
            "h2 {\n" +
            "   font-family: ${Constants.CURRENT_SCRIPT_FONT};\n" +
            "   font-size: 120%;\n" +
            "}\n" +
            ".containerAyah {\n" +
            "   color: black;\n" +
            "   border: 1px solid black;\n" +
            "   border-radius: 50%;\n" +
            "   font-size: 0.7rem;\n" +
            "   display: inline-flex;\n" +
            "   justify-content: center;\n" +
            "   align-items: center;\n" +
            "   padding: 0.4em;\n" +
            "   width: 1em;height: 1em;\n" +
            "}\n" +
            ".containerRuku {\n" +
            "   position: relative;\n" +
            "   display: inline;\n" +
            "}\n" +
            ".ruku {\n" +
            "  position: absolute;\n" +
            "  top: 10%;\n" +
            "  left: 40%;\n" +
            "  font-size: 10px;\n" +
            "}\n" +
            "</style>\n"
    private val script = "<script>\n" +
            "var ayahSelected = null;\n" +

            "function highlightAyah(id) {\n" +
            "if(ayahSelected != null) unHighlightAyah(ayahSelected);\n" +
            "document.getElementById(id).style.color = 'green';\n" +
            "ayahSelected = id;\n" +
            "console.log(id);\n" +
            "}\n" +

            "function unHighlightAyah(id) {\n" +
            "document.getElementById(id).style.color = 'black';\n" +
            "}\n" +

            "function highlightAyahAndTranslation(id) {\n" +
            "if(ayahSelected != null) unHighlightAyahAndTranslation(ayahSelected);\n" +
            "document.getElementById(id).style.color = 'green';\n" +
            "document.getElementById(id + '_tr').style.color = 'green';\n" +
            "ayahSelected = id;\n" +
            "console.log(id);\n" +
            "}\n" +

            "function unHighlightAyahAndTranslation(id) {\n" +
            "document.getElementById(id).style.color = 'black';\n" +
            "document.getElementById(id + '_tr').style.color = 'black';\n" +
            "}\n" +

            "function scrollToAyah(id){\n" +
            "document.getElementById(id).scrollIntoView();\n" +
            "highlightAyah(id);\n" +
            "}\n" +

            "function scrollToAyahAndTranslation(id){\n" +
            "document.getElementById(id).scrollIntoView();\n" +
            "highlightAyahAndTranslation(id);\n" +
            "}\n" +
            "</script>\n"

    private val headEnd = "</head>\n"
    private var bodyStart = "<body>\n"
    private var body = ""
    private val bodyEnd = "</body>\n"
    private val htmlEnd = "</html>\n"

    var zoom = Constants.CURRENT_ZOOM

    var audioRunning = false
    var translationActive = false

    var mp: MediaPlayer? = null
    var surahIndex = 0
    var ayahIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            surahs = it.getSerializable("surahs") as MutableList<Surah>
            edition = it.getString("edition", "")
            surahIndex = it.getInt("surahIndex", 0)
            ayahIndex = it.getInt("ayahNo", 0)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quran_page, container, false)
        wv = view.findViewById(R.id.wv)
        sb = view.findViewById(R.id.sb)
        btPlayback = view.findViewById(R.id.btPlayback)
        btTranslation = view.findViewById(R.id.btTranslation)

        wv.settings.javaScriptEnabled = true
        wv.settings.textZoom = zoom
        sb.progress = zoom

        btTranslation.setOnClickListener {
            translationActive = !translationActive

            mp?.release()
            mp = null
            tts.stop()

            print()
        }

        btPlayback.setOnClickListener {
            if (!audioRunning) {
                audioRunning = true
                mp = MediaPlayer.create(
                    context,
                    Uri.parse(
                        surahs[surahIndex].ayahs[ayahIndex].audio.replace(
                            "https",
                            "http"
                        )
                    )
                )
                mp?.setOnCompletionListener(this@QuranFragment)
                mp?.start()
            } else {
                audioRunning = false
                mp?.release()
                mp = null
                tts.stop()
            }
        }

        tts = TextToSpeech(activity?.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val enVoices: HashSet<Voice> = HashSet()
                for (voice in tts.voices) {
                    if (voice.name.contains(edition) && voice.name.contains("en-gb")) {
                        enVoices.add(voice)
                    } else if (voice.name.contains(edition) && voice.name.contains("ur-pk")) {
                        enVoices.add(voice)
                    }
                }
                tts.voice = enVoices.toArray()[enVoices.size - 3] as Voice?
            }
        }

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
            }

            override fun onDone(utteranceId: String?) {
                updateCurrentAyahIndex()
                CoroutineScope(Dispatchers.Main).launch {
                    wv.loadUrl("javascript:scrollToAyahAndTranslation('surah' + $surahIndex + '_ayah' + $ayahIndex)")
                }
            }

            override fun onError(utteranceId: String?) {
            }

        })

        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int,
                fromUser: Boolean
            ) {
                zoom = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mp?.release()
                mp = null
                tts.stop()

                wv.settings.textZoom = zoom
                print()
            }
        })

        print()

        //      region API Request
//        val repository = Repository()
//        val factory = MainViewModelProviderFactory(repository)
//
//        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
//        viewModel.getQuran()
//        viewModel.quran.observe(this, { response ->
//            surahs = response.data.surahs
//
//            wv.loadData(
//                htmlStart + headStart + style + script + headEnd + bodyStart + body + bodyEnd + htmlEnd,
//                "text/html",
//                "UTF-8"
//            )
//        })
//      endregion

        wv.webViewClient = object : WebViewClient() {
            var progressDialog: ProgressDialog? = null

            override fun onLoadResource(view: WebView?, url: String?) {
                if (progressDialog == null) {
                    progressDialog = ProgressDialog(activity)
                    progressDialog!!.show()
                }
            }

            override fun onPageFinished(view: WebView, weburl: String) {
                try {
                    if (progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                        progressDialog = null
                    }

                    if (translationActive) {
                        wv.loadUrl("javascript:scrollToAyahAndTranslation('surah' + $surahIndex + '_ayah' + $ayahIndex)")
                    } else {
                        wv.loadUrl("javascript:scrollToAyah('surah' + $surahIndex + '_ayah' + $ayahIndex)")
                    }

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }
        }

        wv.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("WebView", consoleMessage.message())

                val id = consoleMessage.message()

                surahIndex = id.substring(5, id.indexOf('_')).toInt()
                ayahIndex = id.substring(id.indexOf('_') + 5).toInt()

                if (audioRunning) {
                    mp?.release()
                    mp = null
                    tts.stop()

                    mp = MediaPlayer.create(
                        context,
                        Uri.parse(
                            surahs[surahIndex].ayahs[ayahIndex].audio.replace(
                                "https",
                                "http"
                            )
                        )
                    )
                    mp?.setOnCompletionListener(this@QuranFragment)
                    mp?.start()
                }
                return true
            }
        }

        return view
    }

    private fun updateCurrentAyahIndex() {
        val lastAyahIndex = surahs[surahIndex].ayahs.size - 1
        if (ayahIndex < lastAyahIndex) {
            ayahIndex++
        } else {
            ayahIndex = 0
            surahIndex++
        }
    }

    private fun print() {
        body = Utility.getHtmlBody(activity, translationActive)

        wv.loadDataWithBaseURL(
            null,
            htmlStart + headStart + style + script + headEnd + bodyStart + body + bodyEnd + htmlEnd,
            "text/html",
            "UTF-8", null
        ).toString()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            surahs: MutableList<Surah>,
            edition: String,
            surahIndex: Int,
            ayahIndex: Int
        ) =
            QuranFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("surahs", surahs as Serializable)
                    putString("edition", edition)
                    putInt("surahIndex", surahIndex)
                    putInt("ayahIndex", ayahIndex)
                }
            }
    }

    override fun onCompletion(mp1: MediaPlayer?) {
        mp1?.release()

        if (translationActive) {
            val params = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "a")
            }
            tts.speak(
                surahs[surahIndex].ayahs[ayahIndex].translation,
                TextToSpeech.QUEUE_FLUSH,
                params,
                "MyUniqueUtteranceId"
            )
        } else {
            updateCurrentAyahIndex()
            wv.loadUrl("javascript:scrollToAyah('surah' + $surahIndex + '_ayah' + $ayahIndex)")
        }
    }

    override fun onStop() {
        super.onStop()
        mp?.release()
        mp = null
        tts.stop()
    }
}