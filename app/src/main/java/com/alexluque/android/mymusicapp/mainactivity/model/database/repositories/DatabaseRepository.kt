package com.alexluque.android.mymusicapp.mainactivity.model.database.repositories

import com.alexluque.android.mymusicapp.mainactivity.model.database.daos.ArtistDao
import com.alexluque.android.mymusicapp.mainactivity.model.database.daos.SongDao
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song

class DatabaseRepository(private val artistDao: ArtistDao, private val songDao: SongDao) {

    // Artists
    suspend fun getFavouriteArtists(): List<Artist> = artistDao.getFavouriteArtists()

    suspend fun artistExists(artistId: Long): Boolean = artistDao.getArtistId(artistId).isNotEmpty()

    suspend fun insertArtist(artist: Artist): Long = artistDao.insert(artist)

    suspend fun deleteArtist(artist: Artist) = artistDao.delete(artist)

    // Songs
    suspend fun artistHasSongs(artistId: Long): Boolean = songDao.getNumberOfSongs(artistId) > 0

    suspend fun getArtistSongs(artistId: Long): Int = songDao.getArtistSongs(artistId).count()

    suspend fun getSongs(): List<Song> = songDao.getSongs()

    suspend fun insertSong(song: Song): Long = songDao.insert(song)

    suspend fun deleteSong(song: Song) = songDao.delete(song)
}