package com.alexluque.android.mymusicapp.mainactivity.model.network.repositories

import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService

suspend fun getArtist(artistName: String) =
    RetrofitBuilder.deezerInstance
        .create(DeezerArtistService::class.java)
        .getArtist(artistName)
        .data
        .firstOrNull()

suspend fun getSongs(artistName: String) =
    RetrofitBuilder.deezerInstance
        .create(DeezerArtistService::class.java)
        .getSongs(artistName)