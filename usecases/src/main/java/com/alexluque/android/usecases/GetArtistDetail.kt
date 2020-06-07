package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.domain.ArtistDetail
import retrofit2.Retrofit

class GetArtistDetail(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, artistName: String): ArtistDetail? = repository.getArtistDetail(retrofit, artistName)
}