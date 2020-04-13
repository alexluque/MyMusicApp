package com.example.android.domain

data class RecommendedArtist(
    val name: String,
    val genre: String,
    val imageUrl: String = String()
)