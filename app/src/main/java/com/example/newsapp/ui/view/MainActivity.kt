package com.example.newsapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var tabLayout: TabLayout? = null
    //private var viewPager: ViewPager2? = null

    private val categories = listOf<String>(
        "Politics",
        "Sport",
        "Nature",
        "Science"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.category_tab_layout)

        /*TabLayoutMediator(tabLayout!!, viewPager!!){tab, pos->
            tab.text = categories[pos]
        }.attach()
        */
    }
}