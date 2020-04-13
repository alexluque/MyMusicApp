package com.example.android.data.repositories

import com.example.android.data.datasources.RemoteDataSource
import com.example.android.domain.RecommendedArtist

class RecommendedArtistsRepository(
    private val remoteDS: RemoteDataSource
) {

    suspend fun getRecommendedArtists(country: String): List<RecommendedArtist> =
        remoteDS.getArtistsByLocation(country)
}