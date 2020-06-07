package com.alexluque.android.domain

data class FavouriteArtist(
    val id: Long,
    val name: String,
    val imageUrl: String
) {
    var favouriteSongs: Int = 0
    var genre: String? = String()
    var regionAndCountry: String = String()
}
