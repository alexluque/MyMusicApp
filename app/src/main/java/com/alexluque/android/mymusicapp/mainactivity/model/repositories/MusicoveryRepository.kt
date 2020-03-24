package com.alexluque.android.mymusicapp.mainactivity.model.repositories

import com.alexluque.android.mymusicapp.mainactivity.model.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.services.MusicoveryArtistService
import java.util.*

suspend fun getArtistsByLocation(country: String) =
    RetrofitBuilder.musicoveryInstance
        .create(MusicoveryArtistService::class.java)
        .getArtistsByLocation(country.toLowerCase(Locale.ROOT).trim())