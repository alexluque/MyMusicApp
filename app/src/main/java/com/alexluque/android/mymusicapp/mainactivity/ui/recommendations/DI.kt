package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.data.repositories.GeolocationRepository
import com.example.android.data.repositories.RecommendedArtistsRepository
import com.example.android.usecases.GetArtistDetail
import com.example.android.usecases.GetCountry
import com.example.android.usecases.GetRecommendedArtists
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

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
        getCountry
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