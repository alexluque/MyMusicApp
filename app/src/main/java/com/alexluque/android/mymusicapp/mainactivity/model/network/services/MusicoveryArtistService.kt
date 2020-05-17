package com.alexluque.android.mymusicapp.mainactivity.model.network.services

import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.GetArtistByNameResponse
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.GetArtistResponse
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.GetArtistsByLocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicoveryArtistService {

    @GET("artist.php")
    suspend fun getArtistsByLocation(
        @Query("location") location: String,
        @Query("fct") getFromLocation: String = "getfromlocation",
        @Query("resultsnumber") results: Int = 100
    ): GetArtistsByLocationResponse

    @GET("artist.php")
    suspend fun getArtist(
        @Query("artistname") artistName: String,
        @Query("fct") getFromLocation: String = "search"
    ): GetArtistByNameResponse

    @GET("artist.php")
    suspend fun getArtistInfo(
        @Query("id") mbid: String,
        @Query("fct") getFromLocation: String = "getinfo"
    ): GetArtistResponse?
}