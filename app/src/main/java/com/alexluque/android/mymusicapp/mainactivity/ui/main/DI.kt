package com.alexluque.android.mymusicapp.mainactivity.ui.main

import com.example.android.data.repositories.FavouriteArtistsRepository
import com.example.android.data.repositories.GeolocationRepository
import com.example.android.usecases.GetCountry
import com.example.android.usecases.GetFavouriteArtistSongs
import com.example.android.usecases.GetFavouriteArtists
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {

    @Provides
    fun mainViewModelProvider(
        getFavouriteArtists: GetFavouriteArtists,
        getFavouriteArtistSongs: GetFavouriteArtistSongs,
        getCountry: GetCountry
    ) = MainViewModel(getFavouriteArtists, getFavouriteArtistSongs, getCountry)

    @Provides
    fun getFavouriteArtistsProvider(
        favouriteArtistsRepository: FavouriteArtistsRepository
    ) = GetFavouriteArtists(favouriteArtistsRepository)

    @Provides
    fun getFavouriteArtistSongsProvider(
        favouriteArtistsRepository: FavouriteArtistsRepository
    ) = GetFavouriteArtistSongs(favouriteArtistsRepository)

    @Provides
    fun getCountryProvider(
        geolocationRepository: GeolocationRepository
    ) = GetCountry(geolocationRepository)
}

@Subcomponent(modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
}