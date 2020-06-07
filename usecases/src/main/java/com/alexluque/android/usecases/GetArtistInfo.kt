package com.alexluque.android.usecases

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.domain.Artist
import retrofit2.Retrofit

class GetArtistInfo(
    private val repository: ArtistDetailRepository
) {

    suspend fun invoke(retrofit: Retrofit, mbid: String): Artist? = repository.getArtistInfo(retrofit, mbid)
}
