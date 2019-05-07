package com.amayasan.exploreaway.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.AppConstants
import com.amayasan.exploreaway.model.Model
import com.amayasan.exploreaway.ui.fragment.VenueDetailFragment

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        if (savedInstanceState == null) {
            val venue : Model.Venue = intent.getParcelableExtra(AppConstants.VENUE_OBJ_KEY)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, VenueDetailFragment.newInstance(venue))
                .commitNow()
        }
    }

}
