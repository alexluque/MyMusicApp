package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Song

class GetFavouriteSongs(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(): List<Song> = repository.getFavouriteSongs()
}