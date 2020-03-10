package com.example.android.mymusicapp.mainactivity.model.network.services

import MusicoveryGetByCountryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicoveryArtistService {

    @GET("artist.php")
    fun getArtistsByLocation(
        @Query("location") location: String,
        @Query("fct") getFromLocation: String = "getfromlocation"
    ): Call<MusicoveryGetByCountryResponse>
}