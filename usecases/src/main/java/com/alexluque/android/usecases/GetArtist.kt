package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.domain.Artist
import retrofit2.Retrofit

class GetArtist(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, artistName: String): Artist = repository.getArtist(retrofit, artistName)
}