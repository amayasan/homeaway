package com.amayasan.exploreaway

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amayasan.exploreaway.ui.fragment.SearchFragment

class ExploreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.explore_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }

}
