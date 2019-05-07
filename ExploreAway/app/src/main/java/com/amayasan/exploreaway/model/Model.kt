package com.amayasan.exploreaway.model

import android.os.Parcelable
import com.amayasan.exploreaway.AppConstants
import kotlinx.android.parcel.Parcelize

object Model {
    data class Result(val response : Response)

    data class Response(val venues : List<Venue>)

    @Parcelize
    data class Venue(val id : String,
                     val name : String,
                     val location : Location,
                     val categories : List<Category>) : Parcelable

    @Parcelize
    data class Location(val address : String,
                        val crossStreet : String?,
                        val lat : Double,
                        val lng : Double,
                        val postalCode : String,
                        val cc : String,
                        val city : String,
                        val state : String,
                        val country : String) : Parcelable {

        fun distanceFromDowntownSeattle() : String {
            var results = FloatArray(1)
            android.location.Location.distanceBetween(lat,lng, AppConstants.DOWNTOWN_SEATTLE_LAT, AppConstants.DOWNTOWN_SEATTLE_LNG, results)

            return results[0].toBigDecimal().toString()
        }
    }

    @Parcelize
    data class Category(val id : String,
                        val name : String,
                        val pluralName : String,
                        val shortName : String,
                        val icon : Icon) : Parcelable

    @Parcelize
    data class Icon(val prefix : String,
                    val suffix : String) : Parcelable
}

