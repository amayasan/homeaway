package com.amayasan.exploreaway.database

import androidx.room.*
import com.amayasan.exploreaway.model.Model
import io.reactivex.Single

@Dao
interface VenueDao {

    @Query("SELECT * from favvenue WHERE id = :id LIMIT 1")
    fun getById(id : String): Model.FavVenue?

    @Query("SELECT * from favvenue WHERE id = :id")
    fun getByIdSingle(id : String): Single<Model.FavVenue>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(venue : Model.FavVenue)

    @Delete
    fun delete(venue : Model.FavVenue)
}