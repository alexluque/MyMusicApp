package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.domain.Song

class GetFavouriteSongs(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(): List<Song> = repository.getFavouriteSongs()
}