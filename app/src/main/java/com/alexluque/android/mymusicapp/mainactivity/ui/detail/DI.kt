package com.alexluque.android.mymusicapp.mainactivity.ui.detail

import com.alexluque.android.mymusicapp.mainactivity.di.ListenersModule
import com.alexluque.android.mymusicapp.mainactivity.ui.main.LocationRecommendationsListener
import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.usecases.HandleFavourite
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class DetailActivityModule(private val artistName: String?) {

    @Provides
    fun detailViewModelProvider(
        handleFavourite: HandleFavourite
    ) = ArtistDetailViewModel(artistName, handleFavourite)

    @Provides
    fun handleFavouriteProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = HandleFavourite(artistDetailRepository)
}

@Subcomponent(modules = [(DetailActivityModule::class), (ListenersModule::class)])
interface DetailActivityComponent {
    val detailViewModel: ArtistDetailViewModel
    val locationListener: LocationRecommendationsListener
}