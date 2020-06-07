package com.alexluque.android.data.repositories

import com.alexluque.android.data.datasources.LocalDataSource
import com.alexluque.android.domain.FavouriteArtist

class FavouriteArtistsRepository(
    private val localDS: LocalDataSource
) {

    suspend fun getFavouriteArtists() : List<FavouriteArtist> = localDS.getFavouriteArtists()

    suspend fun getNumberOfSongs(artistId: Long): Int = localDS.countArtistSongs(artistId)
}