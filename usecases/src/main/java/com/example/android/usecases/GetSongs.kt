package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Song
import retrofit2.Retrofit

class GetSongs(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, artistName: String): List<Song> = repository.getArtistSongs(retrofit, artistName)
}