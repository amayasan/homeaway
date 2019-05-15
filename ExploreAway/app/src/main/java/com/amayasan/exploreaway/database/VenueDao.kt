package com.amayasan.exploreaway.database

import androidx.room.*
import com.amayasan.exploreaway.model.Model

@Dao
interface VenueDao {

    @Query("SELECT * from favvenue WHERE id = :id")
    fun getVenueById(id : String): Model.FavVenue?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(venue : Model.FavVenue)

    @Delete
    suspend fun delete(venue : Model.FavVenue)
}