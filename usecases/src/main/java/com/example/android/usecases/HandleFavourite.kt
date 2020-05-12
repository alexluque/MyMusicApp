package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.*
import retrofit2.Retrofit

class HandleFavourite(
    private val repository: ArtistDetailRepository
) {

    suspend fun getArtistDetail(retrofit: Retrofit, artistName: String): ArtistDetail? = GetArtistDetail(repository).invoke(retrofit, artistName)

    suspend fun getArtistSongs(retrofit: Retrofit, artistName: String): List<Song> = GetSongs(repository).invoke(retrofit, artistName)

    suspend fun getArtist(retrofit: Retrofit, artistName: String): Artist = GetArtist(repository).invoke(retrofit, artistName)

    suspend fun getArtistInfo(retrofit: Retrofit, mbid: String): ArtistInfo = GetArtistInfo(repository).invoke(retrofit, mbid)

    suspend fun getFavouriteSongs(): List<Song> = GetFavouriteSongs(repository).invoke()

    suspend fun isFavouriteArtist(artistId: Long): Boolean = IsFavouriteArtist(repository).invoke(artistId)

    suspend fun insertArtist(artist: FavouriteArtist): Long = InsertArtist(repository).invoke(artist)

    suspend fun insertSong(song: Song): Long = InsertSong(repository).invoke(song)

    suspend fun deleteSong(song: Song) = DeleteSong(repository).invoke(song)

    suspend fun artistHasSongs(artistId: Long): Boolean = FavouriteArtistHasSongs(repository).invoke(artistId)

    suspend fun deleteArtist(artist: FavouriteArtist) = DeleteArtist(repository).invoke(artist)

}