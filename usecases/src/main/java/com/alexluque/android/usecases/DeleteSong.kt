package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.domain.Song

class DeleteSong(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(song: Song) = repository.deleteSong(song)
}