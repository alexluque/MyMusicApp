package com.alexluque.android.mymusicapp.mainactivity.ui.detail

import com.alexluque.android.mymusicapp.mainactivity.di.ListenersModule
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.main.LocationRecommendationsListener
import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.usecases.HandleFavourite
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@ExperimentalStdlibApi
@Module
class DetailActivityModule {

    @Provides
    fun detailViewModelProvider(
        handleFavourite: HandleFavourite
    ) = ArtistDetailViewModel(handleFavourite, Dispatchers.Main, ConnectivityController.getInstance())

    @Provides
    fun handleFavouriteProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = HandleFavourite(artistDetailRepository)
}

@ExperimentalStdlibApi
@Subcomponent(modules = [(DetailActivityModule::class), (ListenersModule::class)])
interface DetailActivityComponent {
    val detailViewModel: ArtistDetailViewModel
    val locationListener: LocationRecommendationsListener
}