package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository

class IsFavouriteArtist(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(artistId: Long): Boolean = repository.isFavouriteArtist(artistId)
}