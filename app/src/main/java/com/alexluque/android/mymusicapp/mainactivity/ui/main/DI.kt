package com.alexluque.android.mymusicapp.mainactivity.ui.main

import com.alexluque.android.mymusicapp.mainactivity.di.ListenersModule
import com.example.android.data.repositories.FavouriteArtistsRepository
import com.example.android.usecases.GetFavouriteArtistSongs
import com.example.android.usecases.GetFavouriteArtists
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class MainActivityModule {

    @Provides
    fun mainViewModelProvider(
        getFavouriteArtists: GetFavouriteArtists,
        getFavouriteArtistSongs: GetFavouriteArtistSongs
    ) = MainViewModel(getFavouriteArtists, getFavouriteArtistSongs, Dispatchers.Main)

    @Provides
    fun getFavouriteArtistsProvider(
        favouriteArtistsRepository: FavouriteArtistsRepository
    ) = GetFavouriteArtists(favouriteArtistsRepository)

    @Provides
    fun getFavouriteArtistSongsProvider(
        favouriteArtistsRepository: FavouriteArtistsRepository
    ) = GetFavouriteArtistSongs(favouriteArtistsRepository)
}

@Subcomponent(modules = [(MainActivityModule::class), (ListenersModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
    val locationLister: LocationRecommendationsListener
}