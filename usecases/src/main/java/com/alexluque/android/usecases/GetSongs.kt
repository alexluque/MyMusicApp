package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.domain.Song
import retrofit2.Retrofit

class GetSongs(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, artistName: String): List<Song> = repository.getArtistSongs(retrofit, artistName)
}