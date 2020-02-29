package com.example.android.mymusicapp.mainactivity.network.services

import DeezerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface DeezerArtistService {

    @GET("search")
    fun getSongs(@Query("q") artist: String): Call<DeezerResponse>
}