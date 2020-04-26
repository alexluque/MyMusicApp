package com.example.android.usecases

import com.example.android.data.repositories.FavouriteArtistsRepository
import com.example.android.domain.FavouriteArtist

class GetFavouriteArtists(
    private val repository: FavouriteArtistsRepository
) {

    suspend fun invoke(): List<FavouriteArtist> = repository.getFavouriteArtists()
}