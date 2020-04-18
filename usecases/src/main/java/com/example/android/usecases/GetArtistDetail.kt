package com.example.android.usecases

import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.ArtistDetail

class GetArtistDetail(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(artistName: String): ArtistDetail? = repository.getArtistDetail(artistName)
}