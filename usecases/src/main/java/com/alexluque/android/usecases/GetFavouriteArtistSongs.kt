package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.FavouriteArtistsRepository

class GetFavouriteArtistSongs(
    private val repository: FavouriteArtistsRepository
) {

    suspend fun invoke(artistId: Long): Int = repository.getNumberOfSongs(artistId)
}