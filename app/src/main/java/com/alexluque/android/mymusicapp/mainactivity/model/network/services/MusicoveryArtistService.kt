package com.alexluque.android.mymusicapp.mainactivity.model.network.services

import MusicoveryGetByCountryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicoveryArtistService {

    @GET("artist.php")
    suspend fun getArtistsByLocation(
        @Query("location") location: String,
        @Query("fct") getFromLocation: String = "getfromlocation"
    ): MusicoveryGetByCountryResponse
}