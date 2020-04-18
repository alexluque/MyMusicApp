package com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery

data class GetArtistByNameResponse(
    val response: Response,
    val artists: ArtistsByNameResponse
)