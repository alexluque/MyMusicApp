package com.example.android.data.datasources

import com.example.android.domain.FavouriteArtist
import com.example.android.domain.Song

interface LocalDataSource {

    // Artists
    suspend fun getFavouriteArtists(): List<FavouriteArtist>

    suspend fun artistExists(artistId: Long): Boolean

    suspend fun insertArtist(artist: FavouriteArtist): Long

    suspend fun deleteArtist(artist: FavouriteArtist)

    // Songs
    suspend fun artistHasSongs(artistId: Long): Boolean

    suspend fun countArtistSongs(artistId: Long): Int

    suspend fun getSongs(): List<Song>

    suspend fun insertSong(song: Song): Long

    suspend fun deleteSong(song: Song)
}