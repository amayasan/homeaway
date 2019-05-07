package com.amayasan.exploreaway.ui.fragment

import androidx.lifecycle.ViewModel
import com.amayasan.exploreaway.model.Model

class VenueDetailViewModel : ViewModel() {
    var venueId = ""
    lateinit var venue : Model.Venue
}
