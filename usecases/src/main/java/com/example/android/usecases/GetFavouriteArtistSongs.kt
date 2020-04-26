package com.example.android.usecases

import com.example.android.data.repositories.FavouriteArtistsRepository

class GetFavouriteArtistSongs(
    private val repository: FavouriteArtistsRepository
) {

    suspend fun invoke(artistId: Long): Int = repository.getNumberOfSongs(artistId)
}