package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.domain.FavouriteArtist

class DeleteArtist(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(artist: FavouriteArtist) = repository.deleteArtist(artist)
}