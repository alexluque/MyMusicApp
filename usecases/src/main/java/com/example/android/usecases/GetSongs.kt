package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Song

class GetSongs(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(artistName: String): List<Song> = repository.getArtistSongs(artistName)
}