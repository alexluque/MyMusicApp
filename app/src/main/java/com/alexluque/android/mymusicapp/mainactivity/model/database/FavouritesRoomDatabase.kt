package com.alexluque.android.mymusicapp.mainactivity.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.daos.ArtistDao
import com.alexluque.android.mymusicapp.mainactivity.model.database.daos.SongDao
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song

@Database(
    entities = [Artist::class, Song::class],
    version = 6,
    exportSchema = false
)
abstract class FavouritesRoomDatabase : RoomDatabase() {

    abstract fun artistDao(): ArtistDao
    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: FavouritesRoomDatabase? = null

        fun getDatabase(context: Context): FavouritesRoomDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavouritesRoomDatabase::class.java,
                    "favourites_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}