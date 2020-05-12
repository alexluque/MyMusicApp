package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.ArtistInfo
import retrofit2.Retrofit

class GetArtistInfo(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, mbid: String): ArtistInfo = repository.getArtistInfo(retrofit, mbid)
}
