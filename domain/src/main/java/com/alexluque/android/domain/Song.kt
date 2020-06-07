package com.alexluque.android.domain

data class Song(
    val id: Long,
    val title: String,
    val album: String? = String(),
    val artistId: Long? = 0
)