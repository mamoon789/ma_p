package com.myisolutions.quran.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myisolutions.quran.R
import com.myisolutions.quran.network.models.Surah
import java.io.Serializable

class QuranBookmarkPagerFragment : Fragment() {
    private lateinit var surahs: MutableList<Surah>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            surahs = it.getSerializable("surahs") as MutableList<Surah>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quran_bookmark_pager, container, false)
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(surahs: MutableList<Surah>, edition: String) =
            QuranBookmarkPagerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("surahs", surahs as Serializable)
                    putString("edition", edition)
                }
            }
    }
}