package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.FavouriteArtistsRepository
import com.alexluque.android.domain.FavouriteArtist

class GetFavouriteArtists(
    private val repository: FavouriteArtistsRepository
) {

    suspend fun invoke(): List<FavouriteArtist> = repository.getFavouriteArtists()
}