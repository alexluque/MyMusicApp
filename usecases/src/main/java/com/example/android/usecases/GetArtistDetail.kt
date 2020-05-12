package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.ArtistDetail
import retrofit2.Retrofit

class GetArtistDetail(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, artistName: String): ArtistDetail? = repository.getArtistDetail(retrofit, artistName)
}