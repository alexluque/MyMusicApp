package com.example.android.data.datasources

import com.example.android.domain.ArtistDetail
import com.example.android.domain.RecommendedArtist
import com.example.android.domain.Song

interface RemoteDataSource {

    suspend fun getArtist(artistName: String): ArtistDetail?

    suspend fun getSongs(artistName: String): List<Song>

    suspend fun getArtistsByLocation(country: String): List<RecommendedArtist>
}