package com.example.android.usecases

import com.example.android.data.repositories.RecommendedArtistsRepository
import com.example.android.domain.RecommendedArtist
import retrofit2.Retrofit

class GetRecommendedArtists(
    private val repository: RecommendedArtistsRepository
) {

    suspend fun invoke(retrofit: Retrofit, country: String): List<RecommendedArtist> = repository.getRecommendedArtists(retrofit, country)
}