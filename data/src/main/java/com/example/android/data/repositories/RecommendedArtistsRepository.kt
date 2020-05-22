package com.example.android.data.repositories

import com.example.android.data.datasources.RemoteDataSource
import com.example.android.domain.RecommendedArtist
import retrofit2.Retrofit

class RecommendedArtistsRepository(
    private val remoteDS: RemoteDataSource
) {

    suspend fun getRecommendedArtists(retrofit: Retrofit, country: String): List<RecommendedArtist> =
        remoteDS.getArtistsByLocation(retrofit, country)
}