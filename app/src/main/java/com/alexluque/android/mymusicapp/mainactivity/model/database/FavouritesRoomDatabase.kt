package com.alexluque.android.mymusicapp.mainactivity.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.daos.ArtistDao
import com.alexluque.android.mymusicapp.mainactivity.model.database.daos.SongDao
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song

@Database(
    entities = [Artist::class, Song::class],
    version = 7,
    exportSchema = false
)
abstract class FavouritesRoomDatabase : RoomDatabase() {

    abstract fun artistDao(): ArtistDao
    abstract fun songDao(): SongDao
}