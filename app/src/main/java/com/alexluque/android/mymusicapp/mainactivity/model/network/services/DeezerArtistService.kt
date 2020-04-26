package com.alexluque.android.mymusicapp.mainactivity.model.network.services

import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongsData
import retrofit2.http.GET
import retrofit2.http.Query


interface DeezerArtistService {

    @GET("search")
    suspend fun getSongs(
        @Query("q") artist: String,
        @Query("limit") number: Int = 100
    ): SongsData

    @GET("search/artist")
    suspend fun getArtist(@Query("q") artist: String): Artist
}