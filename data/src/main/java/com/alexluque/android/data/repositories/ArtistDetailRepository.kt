package com.alexluque.android.data.repositories

import com.alexluque.android.data.datasources.LocalDataSource
import com.alexluque.android.data.datasources.RemoteDataSource
import com.alexluque.android.domain.Artist
import com.alexluque.android.domain.ArtistDetail
import com.alexluque.android.domain.FavouriteArtist
import com.alexluque.android.domain.Song
import retrofit2.Retrofit

class ArtistDetailRepository(
    private val remoteDS: RemoteDataSource,
    private val localDS: LocalDataSource
) {

    suspend fun getArtistDetail(retrofit: Retrofit, artistName: String): ArtistDetail? = remoteDS.getArtistDetail(retrofit, artistName)

    suspend fun getArtistSongs(retrofit: Retrofit, artistName: String): List<Song> = remoteDS.getSongs(retrofit, artistName)

    suspend fun getArtist(retrofit: Retrofit, artistName: String): Artist = remoteDS.getArtist(retrofit, artistName)

    suspend fun getArtistInfo(retrofit: Retrofit, mbid: String): Artist? = remoteDS.getArtistInfo(retrofit, mbid)

    suspend fun insertArtist(artist: FavouriteArtist): Long = localDS.insertArtist(artist)

    suspend fun deleteArtist(artist: FavouriteArtist ) = localDS.deleteArtist(artist)

    suspend fun insertSong(song: Song): Long = localDS.insertSong(song)

    suspend fun deleteSong(song: Song) = localDS.deleteSong(song)

    suspend fun getFavouriteSongs(): List<Song> = localDS.getSongs()

    suspend fun isFavouriteArtist(artistId: Long): Boolean = localDS.artistExists(artistId)

    suspend fun artistHasSongs(artistId: Long): Boolean = localDS.artistHasSongs(artistId)
}