package com.amayasan.exploreaway.service

import com.amayasan.exploreaway.model.Model
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FoursquareApiService {

    companion object {
        fun create(): FoursquareApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl(FoursquareApiConstants.BASE_URL)
                .build()

            return retrofit.create(FoursquareApiService::class.java)
        }
    }

    @GET("v2/venues/search")
    fun doVenueSearchV2(
        @Query("client_id") client_id: String = FoursquareApiConstants.CLIENT_ID,
        @Query("client_secret") client_secret: String = FoursquareApiConstants.CLIENT_SECRET,
        @Query("near") near: String = FoursquareApiConstants.NEAR,
        @Query("query") query: String,
        @Query("v") v: String = FoursquareApiConstants.VERSION,
        @Query("limit") limit: String = FoursquareApiConstants.LIMIT
    ): Observable<Model.Result>

}