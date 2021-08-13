package com.myisolutions.quran.views

import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.myisolutions.quran.R
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var slidingRootNav: SlidingRootNav

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        slidingRootNav = SlidingRootNavBuilder(this)
            .withToolbarMenuToggle(toolbar)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()

        val setting = findViewById<TextView>(R.id.settings)
        setting.setOnClickListener(this)

        val q = findViewById<TextView>(R.id.textView)
        q.setOnClickListener(this)

        onClick(findViewById(R.id.textView))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textView -> showFragment(QuranNavFragment.newInstance())
            R.id.settings -> showFragment(SettingsFragment.newInstance())
        }
        slidingRootNav.closeMenu();
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}