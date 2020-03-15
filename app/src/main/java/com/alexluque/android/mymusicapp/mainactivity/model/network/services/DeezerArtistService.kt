package com.alexluque.android.mymusicapp.mainactivity.model.network.services

import DeezerResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface DeezerArtistService {

    @GET("search")
    suspend fun getSongs(@Query("q") artist: String): DeezerResponse
}