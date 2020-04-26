package com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer

data class ArtistSongData(
    val id: Int,
    val name: String,
    val link: String,
    val picture: String,
    val picture_small: String,
    val picture_medium: String,
    val picture_big: String,
    val picture_xl: String,
    val tracklist: String,
    val type: String
)