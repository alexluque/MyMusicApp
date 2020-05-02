package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.data.repositories.RecommendedArtistsRepository
import com.example.android.usecases.GetArtistDetail
import com.example.android.usecases.GetRecommendedArtists
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class RecommendationsActivityModule(private val country: String) {

    @Provides
    fun recommendationsViewModelProvider(
        getArtistDetail: GetArtistDetail,
        getRecommendedArtists: GetRecommendedArtists
    ) = RecommendationsViewModel(country, getArtistDetail, getRecommendedArtists)

    @Provides
    fun getArtistDetailProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = GetArtistDetail(artistDetailRepository)

    @Provides
    fun getRecommendedArtistsProvider(
        recommendedArtistsRepository: RecommendedArtistsRepository
    ) = GetRecommendedArtists(recommendedArtistsRepository)
}

@Subcomponent(modules = [(RecommendationsActivityModule::class)])
interface RecommendationsActivityComponent {
    val recommendationsViewModel: RecommendationsViewModel
}