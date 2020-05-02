package com.alexluque.android.mymusicapp.mainactivity.ui.detail

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.data.repositories.GeolocationRepository
import com.example.android.usecases.GetCountry
import com.example.android.usecases.HandleFavourite
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class DetailActivityModule(private val artistName: String?) {

    @Provides
    fun detailViewModelProvider(
        handleFavourite: HandleFavourite,
        getCountry: GetCountry
    ) = ArtistDetailViewModel(artistName, handleFavourite, getCountry)

    @Provides
    fun handleFavouriteProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = HandleFavourite(artistDetailRepository)

    @Provides
    fun getCountryProvider(
        geolocationRepository: GeolocationRepository
    ) = GetCountry(geolocationRepository)
}

@Subcomponent(modules = [(DetailActivityModule::class)])
interface DetailActivityComponent {
    val detailViewModel: ArtistDetailViewModel
}