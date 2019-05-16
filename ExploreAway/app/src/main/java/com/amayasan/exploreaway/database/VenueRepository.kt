package com.amayasan.exploreaway.database

import androidx.annotation.WorkerThread
import com.amayasan.exploreaway.model.Model
import io.reactivex.Single

class VenueRepository(private val venueDao: VenueDao) {

    @WorkerThread
    fun getById(id : String) : Model.FavVenue? {
        return venueDao.getById(id)
    }

    @WorkerThread
    fun getByIdSingle(id : String) : Single<Model.FavVenue> {
        return venueDao.getByIdSingle(id)
    }

    @WorkerThread
    fun insert(venue : Model.FavVenue) {
        venueDao.insert(venue)
    }

    @WorkerThread
    fun delete(venue : Model.FavVenue) {
        venueDao.delete(venue)
    }
}