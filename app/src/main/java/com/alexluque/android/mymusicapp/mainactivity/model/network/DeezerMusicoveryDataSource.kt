package com.alexluque.android.mymusicapp.mainactivity.model.network

import com.alexluque.android.mymusicapp.mainactivity.model.*
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.ArtistsByNameResponse
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import com.example.android.data.datasources.RemoteDataSource
import com.example.android.domain.ArtistDetail
import com.example.android.domain.RecommendedArtist
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Retrofit
import java.io.IOException
import java.util.*
import com.example.android.domain.Artist as DomainArtist
import com.example.android.domain.Song as DomainSong

@Suppress("UNCHECKED_CAST")
class DeezerMusicoveryDataSource : RemoteDataSource {

    override suspend fun getArtistDetail(retrofit: Retrofit, artistName: String): ArtistDetail? =
        try {
            retrofit
                .create(DeezerArtistService::class.java)
                .getArtist(artistName)
                .data
                ?.firstOrNull()
                ?.toArtistDetail()
        } catch (e: IOException) {
            null
        }

    override suspend fun getSongs(retrofit: Retrofit, artistName: String): List<DomainSong> =
        try {
            retrofit
                .create(DeezerArtistService::class.java)
                .getSongs(artistName)
                .data
                .map(SongData::toDomainSong)
        } catch (e: IOException) {
            emptyList<DomainSong>()
        }

    override suspend fun getArtistsByLocation(retrofit: Retrofit, country: String): List<RecommendedArtist> =
        try {
            retrofit
                .create(MusicoveryArtistService::class.java)
                .getArtistsByLocation(country.toLowerCase(Locale.ROOT).trim())
                .artists
                .artist
                .map(Artist::toRecommendedArtist)
        } catch (e: Exception) {
            emptyList<RecommendedArtist>()
        }

    override suspend fun getArtist(retrofit: Retrofit, artistName: String): DomainArtist {
        try {
            val artists = retrofit
                .create(MusicoveryArtistService::class.java)
                .getArtist(artistName)
                .artists

            return when (artists) {
                is Map<*, *> -> {
                    if (artists.isNotEmpty()) {
                        try {
                            val map = artists as AbstractMap<*, *>
                            val firstLevel = map.values.first() as AbstractMap<*, *>
                            val secondLevel = firstLevel.values.first() as AbstractMap<*, *>
                            val thirdLevel = secondLevel.values.first() as LinkedTreeMap<String, String>
                            thirdLevel.toDomainArtist()
                        } catch (e1: ClassCastException) {
                            try {
                                val list = artists.values.first() as List<*>
                                val firstLevel = list.first() as AbstractMap<*, *>
                                val secondLevel = firstLevel.values.first() as AbstractMap<*, *>
                                val thirdLevel = secondLevel.values.first() as LinkedTreeMap<String, String>
                                thirdLevel.toDomainArtist()
                            } catch (e2: ClassCastException) {
                                emptyDomainArtist()
                            }
                        }
                    } else {
                        emptyDomainArtist()
                    }
                }
                is ArtistsByNameResponse -> artists.artist.artists.artist.toDomainArtist()
                else -> emptyDomainArtist()
            }
        } catch (e: IOException) {
            return emptyDomainArtist()
        }
    }

    @ExperimentalStdlibApi
    override suspend fun getArtistInfo(retrofit: Retrofit, mbid: String): DomainArtist? =
        try {
            retrofit
                .create(MusicoveryArtistService::class.java)
                .getArtistInfo(mbid)
                ?.artist
                ?.toDomainArtistInfo()
        } catch (e: IOException) {
            null
        }
}