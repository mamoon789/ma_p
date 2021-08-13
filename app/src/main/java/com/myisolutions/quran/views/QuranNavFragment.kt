package com.myisolutions.quran.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.myisolutions.quran.R
import com.myisolutions.quran.network.models.Quran
import com.myisolutions.quran.network.models.Surah
import com.myisolutions.quran.utils.Constants
import java.io.BufferedReader
import java.io.InputStreamReader

class QuranNavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quran_nav, container, false)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        val quran = getQuran()

        viewPager.adapter = ViewPagerFragmentAdapter(
            activity!!,
            quran.data.surahs,
            quran.data.edition.language
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Surah" else if (position == 1) "Para" else "Bookmark"
        }.attach()

        return view
    }

    private fun getSettings() {
        val sharedPreferences = activity!!.getSharedPreferences(
            "Settings",
            Context.MODE_PRIVATE
        )
        Constants.CURRENT_SCRIPT = sharedPreferences.getString(
            Constants.KEY_SCRIPT,
            Constants.SCRIPTS.keys.toList()[0]
        )!!
        Constants.CURRENT_SCRIPT_FONT = sharedPreferences.getString(
            Constants.KEY_SCRIPT_FONT,
            Constants.AR_FONTS.values.toList()[0]
        )!!
        Constants.CURRENT_TRANSLATION = sharedPreferences.getString(
            Constants.KEY_TRANSLATION,
            Constants.TRANSLATIONS.keys.toList()[0]
        )!!
        Constants.CURRENT_TRANSLATION_FONT = sharedPreferences.getString(
            Constants.KEY_TRANSLATION_FONT,
            Constants.EN_FONTS.values.toList()[0]
        )!!
        Constants.CURRENT_ZOOM = sharedPreferences.getInt(
            Constants.KEY_ZOOM,
            100
        )
    }

    private fun getQuran(): Quran {
        getSettings()
        val quranJson = StringBuilder()
        val inputStream = activity?.assets?.open(
            Constants.CURRENT_SCRIPT + "_" + Constants.CURRENT_TRANSLATION + ".json"
        )!!
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            quranJson.append(line)
            quranJson.append('\n')
        }
        inputStream.close()
        bufferedReader.close()
        return Gson().fromJson(quranJson.toString(), Quran::class.java)
    }

    private class ViewPagerFragmentAdapter(
        fragmentActivity: FragmentActivity,
        val surahs: MutableList<Surah>,
        val edition: String
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return QuranSurahPagerFragment.newInstance(surahs, edition)
                1 -> return QuranParaPagerFragment.newInstance(surahs, edition)
                2 -> return QuranBookmarkPagerFragment.newInstance(surahs, edition)
            }
            return QuranSurahPagerFragment()
        }

        override fun getItemCount(): Int {
            return 3
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = QuranNavFragment()
    }
}