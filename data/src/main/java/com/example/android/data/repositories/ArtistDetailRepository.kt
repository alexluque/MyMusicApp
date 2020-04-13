package com.example.android.data.repositories

import com.example.android.data.datasources.LocalDataSource
import com.example.android.data.datasources.RemoteDataSource
import com.example.android.domain.ArtistDetail
import com.example.android.domain.FavouriteArtist
import com.example.android.domain.Song

class ArtistDetailRepository(
    private val remoteDS: RemoteDataSource,
    private val localDS: LocalDataSource
) {

    suspend fun getArtist(artistName: String): ArtistDetail? = remoteDS.getArtist(artistName)

    suspend fun getArtistSongs(artistName: String): List<Song> = remoteDS.getSongs(artistName)

    suspend fun insertArtist(artist: FavouriteArtist): Long = localDS.insertArtist(artist)

    suspend fun deleteArtist(artist: FavouriteArtist ) = localDS.deleteArtist(artist)

    suspend fun insertSong(song: Song): Long = localDS.insertSong(song)

    suspend fun deleteSong(song: Song) = localDS.deleteSong(song)

    suspend fun getFavouriteSongs(): List<Song> = localDS.getSongs()

    suspend fun isFavouriteArtist(artistId: Long): Boolean = localDS.artistExists(artistId)

    suspend fun artistHasSongs(artistId: Long): Boolean = localDS.artistHasSongs(artistId)
}