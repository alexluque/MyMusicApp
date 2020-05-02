package com.alexluque.android.mymusicapp.mainactivity.model.database.entities

import androidx.room.*

@Entity(
    tableName = "songs",
    indices = [Index(value = ["artist_id"])],
    foreignKeys = [
        ForeignKey(
            entity = Artist::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("artist_id")
        )
    ]
)
data class Song(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val title: String,
    @ColumnInfo(name = "album") val album: String? = String(),
    @ColumnInfo(name = "artist_id") val artistId: Long
)