package com.alexluque.android.mymusicapp.mainactivity.model.network.repositories

import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.ArtistData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongsData
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService

suspend fun getArtist(artistName: String): ArtistData? =
    RetrofitBuilder.deezerInstance
        .create(DeezerArtistService::class.java)
        .getArtist(artistName)
        .data
        .firstOrNull()

suspend fun getSongs(artistName: String): SongsData =
    RetrofitBuilder.deezerInstance
        .create(DeezerArtistService::class.java)
        .getSongs(artistName)