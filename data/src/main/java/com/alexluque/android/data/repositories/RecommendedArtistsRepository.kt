package com.alexluque.android.data.repositories

import com.alexluque.android.data.datasources.RemoteDataSource
import com.alexluque.android.domain.RecommendedArtist
import retrofit2.Retrofit

class RecommendedArtistsRepository(
    private val remoteDS: RemoteDataSource
) {

    suspend fun getRecommendedArtists(retrofit: Retrofit, country: String): List<RecommendedArtist> =
        remoteDS.getArtistsByLocation(retrofit, country)
}