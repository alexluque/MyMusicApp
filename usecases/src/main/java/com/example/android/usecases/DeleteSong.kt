package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Song

class DeleteSong(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(song: Song) = repository.deleteSong(song)
}