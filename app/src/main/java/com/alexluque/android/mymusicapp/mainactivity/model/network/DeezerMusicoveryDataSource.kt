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
import java.lang.ClassCastException
import java.lang.NullPointerException
import java.util.*
import com.example.android.domain.Artist as DomainArtist
import com.example.android.domain.ArtistInfo as DomainArtistInfo
import com.example.android.domain.Song as DomainSong

@Suppress("UNCHECKED_CAST")
class DeezerMusicoveryDataSource : RemoteDataSource {

    override suspend fun getArtistDetail(artistName: String): ArtistDetail? =
        RetrofitBuilder.deezerInstance
            .create(DeezerArtistService::class.java)
            .getArtist(artistName)
            .data
            ?.firstOrNull()
            ?.toArtistDetail()

    override suspend fun getSongs(artistName: String): List<DomainSong> =
        RetrofitBuilder.deezerInstance
            .create(DeezerArtistService::class.java)
            .getSongs(artistName)
            .data
            .map(SongData::toDomainSong)

    override suspend fun getArtistsByLocation(country: String): List<RecommendedArtist> {
        return try {
            RetrofitBuilder.musicoveryInstance
                .create(MusicoveryArtistService::class.java)
                .getArtistsByLocation(country.toLowerCase(Locale.ROOT).trim())
                .artists
                .artist
                .map(Artist::toRecommendedArtist)
        } catch (ex: NullPointerException) {
            emptyList<RecommendedArtist>()
        }
    }

    override suspend fun getArtist(artistName: String): DomainArtist {
        val artists = RetrofitBuilder.musicoveryInstance
            .create(MusicoveryArtistService::class.java)
            .getArtist(artistName)
            .artists

        return when (artists) {
            is Map<*, *> -> {
                if (artists.isNotEmpty())
                    try {
                        ((((artists as AbstractMap<*, *>).values.first() as AbstractMap<*, *>)
                            .values.first() as AbstractMap<*, *>)
                            .values.first() as LinkedTreeMap<String, String>).toDomainArtist()
                    } catch (ex1: ClassCastException) {
                        try {
                            ((((artists.values.first() as List<*>).first() as AbstractMap<*, *>)
                                .values.first() as AbstractMap<*, *>)
                                .values.first() as LinkedTreeMap<String, String>).toDomainArtist()
                        } catch (ex2: ClassCastException) {
                            emptyDomainArtist()
                        }
                    }
                else
                    emptyDomainArtist()
            }
            is ArtistsByNameResponse -> artists.artist.artists.artist.toDomainArtist()
            else -> emptyDomainArtist()
        }
    }

    @ExperimentalStdlibApi
    override suspend fun getArtistInfo(mbid: String): DomainArtistInfo =
        RetrofitBuilder.musicoveryInstance
            .create(MusicoveryArtistService::class.java)
            .getArtistInfo(mbid)
            .artist
            .toDomainArtistInfo()
}