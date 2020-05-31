package com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer

data class AlbumData(
    val id: Int = 0,
    val title: String = String(),
    val cover: String = String(),
    val cover_small: String = String(),
    val cover_medium: String = String(),
    val cover_big: String = String(),
    val cover_xl: String = String(),
    val tracklist: String = String(),
    val type: String = String()
)