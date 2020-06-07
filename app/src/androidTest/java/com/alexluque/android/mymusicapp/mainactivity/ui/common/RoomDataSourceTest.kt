package com.alexluque.android.mymusicapp.mainactivity.ui.common

import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.toDomainSong
import com.alexluque.android.mymusicapp.mainactivity.model.toRoomArtist
import com.alexluque.android.mymusicapp.mainactivity.model.toRoomSong
import com.alexluque.android.data.datasources.LocalDataSource
import com.alexluque.android.domain.FavouriteArtist
import com.alexluque.android.domain.Song

class RoomDataSourceTest(db: FavouritesRoomDatabase) : LocalDataSource {

    private val artistDao = db.artistDao()
    private val songDao = db.songDao()

    override suspend fun getFavouriteArtists(): List<FavouriteArtist> = listOf(
        FavouriteArtist(
            1245,
            "Trivium",
            "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/500x500-000000-80-0-0.jpg"
        )
    )

    override suspend fun artistExists(artistId: Long): Boolean = artistDao.getArtistId(artistId).isNotEmpty()

    override suspend fun insertArtist(artist: FavouriteArtist): Long = artistDao.insert(artist.toRoomArtist())

    override suspend fun deleteArtist(artist: FavouriteArtist) = artistDao.delete(artist.toRoomArtist())

    override suspend fun artistHasSongs(artistId: Long): Boolean = songDao.getNumberOfSongs(artistId) > 0

    override suspend fun countArtistSongs(artistId: Long): Int = songDao.getArtistSongs(artistId).count()

    override suspend fun getSongs(): List<Song> = songDao.getSongs().map(com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song::toDomainSong)

    override suspend fun insertSong(song: Song): Long = songDao.insert(song.toRoomSong())

    override suspend fun deleteSong(song: Song) = songDao.delete(song.toRoomSong())
}