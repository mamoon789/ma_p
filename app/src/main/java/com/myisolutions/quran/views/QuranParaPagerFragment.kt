package com.myisolutions.quran.views

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myisolutions.quran.R
import com.myisolutions.quran.network.models.Ayah
import com.myisolutions.quran.network.models.Surah
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class QuranParaPagerFragment : Fragment() {
    lateinit var surahs: MutableList<Surah>
    lateinit var ayahs: MutableList<Ayah>
    lateinit var edition: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            surahs = it.getSerializable("surahs") as MutableList<Surah>
            edition = it.getString("edition", "")
            ayahs = surahs.flatMap { it.ayahs }.distinctBy { it.juz } as MutableList<Ayah>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quran_para_pager, container, false)
        val rvParas = view.findViewById<RecyclerView>(R.id.rvParas)
        rvParas.adapter = Adapter()
        rvParas.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return view
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.para_row, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.surahIndex = position
            holder.ayahIndex = 0
            holder.tvPara.text = "PARA " + ayahs.get(position).juz.toString()
        }

        override fun getItemCount(): Int {
            return ayahs.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

            var surahIndex: Int = 0
            var ayahIndex: Int = 0
            val tvPara: TextView = view.findViewById(R.id.tvPara)

            init {
                view.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(
                    R.id.container,
                    QuranFragment.newInstance(surahs, edition, surahIndex, ayahIndex)
                );
                transaction?.addToBackStack(null);
                transaction?.commit();
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(surahs: MutableList<Surah>, edition: String) =
            QuranParaPagerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("surahs", surahs as Serializable)
                    putString("edition", edition)
                }
            }
    }
}