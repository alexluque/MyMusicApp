package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository

class FavouriteArtistHasSongs(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(artistId: Long): Boolean = repository.artistHasSongs(artistId)
}