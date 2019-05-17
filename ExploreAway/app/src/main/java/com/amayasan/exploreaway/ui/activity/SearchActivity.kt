package com.amayasan.exploreaway.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.ui.fragment.VenueSearchFragment

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, VenueSearchFragment.newInstance())
                .commitNow()
        }
    }
}
