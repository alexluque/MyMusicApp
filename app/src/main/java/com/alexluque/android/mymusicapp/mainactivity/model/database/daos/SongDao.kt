package com.alexluque.android.mymusicapp.mainactivity.model.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song

@Dao
interface SongDao {

    @Query("SELECT COUNT(id) FROM songs WHERE artist_id = :artistId")
    suspend fun getNumberOfSongs(artistId: Long): Int

    @Query("SELECT * FROM songs WHERE artist_id = :artistId")
    suspend fun getArtistSongs(artistId: Long): List<Song>

    @Query("SELECT * FROM songs")
    suspend fun getSongs(): List<Song>

    @Insert
    suspend fun insert(song: Song): Long

    @Delete
    suspend fun delete(song: Song)
}