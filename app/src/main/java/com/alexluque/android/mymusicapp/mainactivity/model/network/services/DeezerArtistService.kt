package com.alexluque.android.mymusicapp.mainactivity.model.network.services

import Artist
import SongsData
import retrofit2.http.GET
import retrofit2.http.Query


interface DeezerArtistService {

    @GET("search")
    suspend fun getSongs(@Query("q") artist: String): SongsData

    @GET("search/artist")
    suspend fun getArtist(@Query("q") artist: String): Artist
}