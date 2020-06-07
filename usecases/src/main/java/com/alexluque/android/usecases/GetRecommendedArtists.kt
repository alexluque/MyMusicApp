package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.RecommendedArtistsRepository
import com.alexluque.android.domain.RecommendedArtist
import retrofit2.Retrofit

class GetRecommendedArtists(
    private val repository: RecommendedArtistsRepository
) {

    suspend fun invoke(retrofit: Retrofit, country: String): List<RecommendedArtist> = repository.getRecommendedArtists(retrofit, country)
}