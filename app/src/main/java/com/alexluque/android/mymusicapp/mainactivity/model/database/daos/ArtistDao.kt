package com.alexluque.android.mymusicapp.mainactivity.model.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists ORDER BY name ASC")
    suspend fun getFavouriteArtists(): List<Artist>

    @Query("SELECT id from artists WHERE id = :artistId LIMIT 1")
    suspend fun getArtistId(artistId: Long): List<Long>

    @Insert
    suspend fun insert(artist: Artist): Long

    @Delete
    suspend fun delete(artist: Artist)
}