package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository

class IsFavouriteArtist(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(artistId: Long): Boolean = repository.isFavouriteArtist(artistId)
}