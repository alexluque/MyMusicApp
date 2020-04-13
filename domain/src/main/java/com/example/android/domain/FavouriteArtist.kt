package com.example.android.domain

data class FavouriteArtist(
    val id: Long,
    val name: String,
    val imageUrl: String
) {
    var favouriteSongs: Int = 0
}
