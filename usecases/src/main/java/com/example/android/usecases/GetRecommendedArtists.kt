package com.example.android.usecases

import com.example.android.data.repositories.RecommendedArtistsRepository
import com.example.android.domain.RecommendedArtist

class GetRecommendedArtists(
    private val repository: RecommendedArtistsRepository
) {

    suspend fun invoke(country: String): List<RecommendedArtist> = repository.getRecommendedArtists(country)
}