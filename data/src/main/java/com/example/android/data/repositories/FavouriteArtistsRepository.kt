package com.example.android.data.repositories

import com.example.android.data.datasources.LocalDataSource
import com.example.android.domain.FavouriteArtist

class FavouriteArtistsRepository(
    private val localDS: LocalDataSource
) {

    suspend fun getFavouriteArtists() : List<FavouriteArtist> = localDS.getFavouriteArtists()

    suspend fun getNumberOfSongs(artistId: Long): Int = localDS.countArtistSongs(artistId)
}