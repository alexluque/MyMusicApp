package com.example.android.data.datasources

import com.example.android.domain.*

interface RemoteDataSource {

    suspend fun getArtistDetail(artistName: String): ArtistDetail?

    suspend fun getSongs(artistName: String): List<Song>

    suspend fun getArtistsByLocation(country: String): List<RecommendedArtist>

    suspend fun getArtist(artistName: String): Artist

    suspend fun getArtistInfo(mbid: String): ArtistInfo
}