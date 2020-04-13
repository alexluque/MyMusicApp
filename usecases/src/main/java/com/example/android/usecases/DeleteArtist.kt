package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.FavouriteArtist

class DeleteArtist(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(artist: FavouriteArtist) = repository.deleteArtist(artist)
}