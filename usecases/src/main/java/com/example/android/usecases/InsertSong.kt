package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Song

class InsertSong(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(song: Song) = repository.insertSong(song)
}