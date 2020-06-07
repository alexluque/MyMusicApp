package com.alexluque.android.mymusicapp.mainactivity.ui.main

import com.alexluque.android.mymusicapp.mainactivity.di.ListenersModule
import com.alexluque.android.data.repositories.FavouriteArtistsRepository
import com.alexluque.android.usecases.GetFavouriteArtistSongs
import com.alexluque.android.usecases.GetFavouriteArtists
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

@ExperimentalStdlibApi
@Subcomponent(modules = [(MainActivityModule::class), (ListenersModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
    val locationLister: LocationRecommendationsListener
}