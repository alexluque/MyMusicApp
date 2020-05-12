package com.example.android.domain

data class RecommendedArtist(
    val name: String,
    val genre: Any?,
    val country: Any?,
    val imageUrl: String = String()
)