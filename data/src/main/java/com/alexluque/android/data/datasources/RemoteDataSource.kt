package com.alexluque.android.data.datasources

import com.alexluque.android.domain.Artist
import com.alexluque.android.domain.ArtistDetail
import com.alexluque.android.domain.RecommendedArtist
import com.alexluque.android.domain.Song
import retrofit2.Retrofit

interface RemoteDataSource {

    suspend fun getArtistDetail(retrofit: Retrofit, artistName: String): ArtistDetail?

    suspend fun getSongs(retrofit: Retrofit, artistName: String): List<Song>

    suspend fun getArtistsByLocation(retrofit: Retrofit, country: String): List<RecommendedArtist>

    suspend fun getArtist(retrofit: Retrofit, artistName: String): Artist

    suspend fun getArtistInfo(retrofit: Retrofit, mbid: String): Artist?
}