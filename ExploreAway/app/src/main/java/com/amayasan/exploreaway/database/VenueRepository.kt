package com.amayasan.exploreaway.database

import androidx.annotation.WorkerThread
import com.amayasan.exploreaway.model.Model

class VenueRepository(private val venueDao: VenueDao) {

    @WorkerThread
    fun getById(id : String) : Model.FavVenue? {
        return venueDao.getVenueById(id)
    }

    @WorkerThread
    suspend fun insert(venue : Model.FavVenue) {
        venueDao.insert(venue)
    }

    @WorkerThread
    suspend fun delete(venue : Model.FavVenue) {
        venueDao.delete(venue)
    }
}