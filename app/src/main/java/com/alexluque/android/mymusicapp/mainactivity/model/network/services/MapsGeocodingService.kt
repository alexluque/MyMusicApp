package com.alexluque.android.mymusicapp.mainactivity.model.network.services

import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.geocoding.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MapsGeocodingService {

    @GET("json")
    suspend fun getAddresses(
        @Query("latlng") latlng: String,
        @Query("key") key: String
    ): GeocodingResponse
}