package com.alexluque.android.mymusicapp.mainactivity.model.database

import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.toDomainArtist
import com.alexluque.android.mymusicapp.mainactivity.model.toDomainSong
import com.alexluque.android.mymusicapp.mainactivity.model.toRoomArtist
import com.alexluque.android.mymusicapp.mainactivity.model.toRoomSong
import com.example.android.data.datasources.LocalDataSource
import com.example.android.domain.FavouriteArtist
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song as RoomSong
import com.example.android.domain.Song as DomainSong

class RoomDataSource(db: FavouritesRoomDatabase) : LocalDataSource {

    private val artistDao = db.artistDao()
    private val songDao = db.songDao()

    override suspend fun getFavouriteArtists(): List<FavouriteArtist> = artistDao.getFavouriteArtists().map(Artist::toDomainArtist)

    override suspend fun artistExists(artistId: Long): Boolean = artistDao.getArtistId(artistId).isNotEmpty()

    override suspend fun insertArtist(artist: FavouriteArtist): Long = artistDao.insert(artist.toRoomArtist())

    override suspend fun deleteArtist(artist: FavouriteArtist) = artistDao.delete(artist.toRoomArtist())

    override suspend fun artistHasSongs(artistId: Long): Boolean = songDao.getNumberOfSongs(artistId) > 0

    override suspend fun countArtistSongs(artistId: Long): Int = songDao.getArtistSongs(artistId).count()

    override suspend fun getSongs(): List<DomainSong> = songDao.getSongs().map(RoomSong::toDomainSong)

    override suspend fun insertSong(song: DomainSong): Long = songDao.insert(song.toRoomSong())

    override suspend fun deleteSong(song: DomainSong) = songDao.delete(song.toRoomSong())
}