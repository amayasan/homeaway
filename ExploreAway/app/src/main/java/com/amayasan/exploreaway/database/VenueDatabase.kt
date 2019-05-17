package com.amayasan.exploreaway.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amayasan.exploreaway.model.Model

@Database(entities = arrayOf(Model.FavVenue::class), version = 1)
abstract class VenueDatabase : RoomDatabase() {

    abstract fun venueDao(): VenueDao

    companion object {
        @Volatile
        private var INSTANCE: VenueDatabase? = null

        fun getDatabase(context: Context): VenueDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VenueDatabase::class.java,
                    "venue_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}