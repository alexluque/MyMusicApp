package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.data.repositories.GeolocationRepository
import com.alexluque.android.data.repositories.RecommendedArtistsRepository
import com.alexluque.android.usecases.GetArtistDetail
import com.alexluque.android.usecases.GetCountry
import com.alexluque.android.usecases.GetRecommendedArtists
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.Dispatchers

@Module
class RecommendationsActivityModule(
    private val latitude: String,
    private val longitude: String,
    private val mapsKey: String
) {

    @Provides
    fun recommendationsViewModelProvider(
        getArtistDetail: GetArtistDetail,
        getRecommendedArtists: GetRecommendedArtists,
        getCountry: GetCountry
    ) = RecommendationsViewModel(
        latitude,
        longitude,
        mapsKey,
        getArtistDetail,
        getRecommendedArtists,
        getCountry,
        Dispatchers.Main
    )

    @Provides
    fun getArtistDetailProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = GetArtistDetail(artistDetailRepository)

    @Provides
    fun getRecommendedArtistsProvider(
        recommendedArtistsRepository: RecommendedArtistsRepository
    ) = GetRecommendedArtists(recommendedArtistsRepository)

    @Provides
    fun getCountryProvider(
        geolocationRepository: GeolocationRepository
    ) = GetCountry(geolocationRepository)
}

@Subcomponent(modules = [(RecommendationsActivityModule::class)])
interface RecommendationsActivityComponent {
    val recommendationsViewModel: RecommendationsViewModel
}