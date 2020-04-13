package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.ArtistDetail
import com.example.android.domain.FavouriteArtist
import com.example.android.domain.Song

class HandleFavourite(
    private val repository: ArtistDetailRepository
) {

    suspend fun getArtist(artistName: String): ArtistDetail? = GetArtist(repository).invoke(artistName)

    suspend fun getArtistSongs(artistName: String): List<Song> = GetSongs(repository).invoke(artistName)

    suspend fun getFavouriteSongs(): List<Song> = GetFavouriteSongs(repository).invoke()

    suspend fun isFavouriteArtist(artistId: Long): Boolean = IsFavouriteArtist(repository).invoke(artistId)

    suspend fun insertArtist(artist: FavouriteArtist): Long = InsertArtist(repository).invoke(artist)

    suspend fun insertSong(song: Song): Long = InsertSong(repository).invoke(song)

    suspend fun deleteSong(song: Song) = DeleteSong(repository).invoke(song)

    suspend fun artistHasSongs(artistId: Long): Boolean = FavouriteArtistHasSongs(repository).invoke(artistId)

    suspend fun deleteArtist(artist: FavouriteArtist) = DeleteArtist(repository).invoke(artist)

}