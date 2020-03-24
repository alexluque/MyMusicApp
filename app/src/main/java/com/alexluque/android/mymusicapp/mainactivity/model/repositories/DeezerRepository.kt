package com.alexluque.android.mymusicapp.mainactivity.model.repositories

import com.alexluque.android.mymusicapp.mainactivity.model.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.services.DeezerArtistService

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