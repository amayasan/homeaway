package com.amayasan.exploreaway.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amayasan.exploreaway.model.Model

class VenueSearchViewModel : ViewModel() {
    val venues : MutableLiveData<List<Model.Venue>> = MutableLiveData()
}
