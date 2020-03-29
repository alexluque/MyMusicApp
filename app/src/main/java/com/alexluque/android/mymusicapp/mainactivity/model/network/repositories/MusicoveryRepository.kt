package com.alexluque.android.mymusicapp.mainactivity.model.network.repositories

import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import java.util.*

suspend fun getArtistsByLocation(country: String) =
    RetrofitBuilder.musicoveryInstance
        .create(MusicoveryArtistService::class.java)
        .getArtistsByLocation(country.toLowerCase(Locale.ROOT).trim())