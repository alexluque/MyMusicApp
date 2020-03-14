package com.alexluque.android.mymusicapp.mainactivity.model.network.services

import GeocodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MapsGeocodingService {

    @GET("json")
    fun getAddresses(
        @Query("latlng") latlng: String,
        @Query("key") key: String
    ): Call<GeocodingResponse>
}