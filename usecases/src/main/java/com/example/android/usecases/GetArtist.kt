package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Artist
import retrofit2.Retrofit

class GetArtist(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, artistName: String): Artist = repository.getArtist(retrofit, artistName)
}