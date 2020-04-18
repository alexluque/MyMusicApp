package com.alexluque.android.mymusicapp.mainactivity.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
class Artist(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "genre") val genre: String? = String(),
    @ColumnInfo(name = "region_and_country") val regionAndCountry: String
) {
    var favouriteSongs: Int = 0
}