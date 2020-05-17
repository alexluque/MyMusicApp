package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Artist
import retrofit2.Retrofit

class GetArtistInfo(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, mbid: String): Artist? = repository.getArtistInfo(retrofit, mbid)
}
