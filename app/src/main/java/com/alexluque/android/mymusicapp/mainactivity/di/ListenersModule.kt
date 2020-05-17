package com.alexluque.android.mymusicapp.mainactivity.di

import android.content.Context
import com.alexluque.android.mymusicapp.mainactivity.ui.main.LocationRecommendationsListener
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides

@ExperimentalStdlibApi
@Module
class ListenersModule(
    private val context: Context,
    private val fusedClient: FusedLocationProviderClient
) {

    @Provides
    fun locationRecommendationsListenerProvider() = LocationRecommendationsListener(context, fusedClient)
}