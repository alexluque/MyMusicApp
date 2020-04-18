package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.ArtistInfo

class GetArtistInfo(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(mbid: String): ArtistInfo = repository.getArtistInfo(mbid)
}
