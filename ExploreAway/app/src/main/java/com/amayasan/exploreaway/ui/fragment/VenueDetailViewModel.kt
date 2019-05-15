package com.amayasan.exploreaway.ui.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amayasan.exploreaway.database.VenueDatabase
import com.amayasan.exploreaway.database.VenueRepository
import com.amayasan.exploreaway.model.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VenueDetailViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var venue : Model.Venue
    var isFavorite : MutableLiveData<Boolean> = MutableLiveData(false)

    private val repository: VenueRepository

    init {
        val venueDao = VenueDatabase.getDatabase(application).venueDao()
        repository = VenueRepository(venueDao)
    }

    fun findById() = viewModelScope.launch(Dispatchers.IO) {
        isFavorite.postValue(repository.getById(venue.id) != null)
    }

    fun insert() = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(Model.FavVenue(venue.id))
    }

    fun delete() = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(Model.FavVenue(venue.id))
    }
}
