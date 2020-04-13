package com.alexluque.android.mymusicapp.mainactivity.model.network

import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.MusicoveryArtist
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import com.alexluque.android.mymusicapp.mainactivity.model.toArtistDetail
import com.alexluque.android.mymusicapp.mainactivity.model.toDomainSong
import com.alexluque.android.mymusicapp.mainactivity.model.toRecommendedArtist
import com.example.android.data.datasources.RemoteDataSource
import com.example.android.domain.ArtistDetail
import com.example.android.domain.RecommendedArtist
import java.util.*
import com.example.android.domain.Song as DomainSong

class DeezerMusicoveryDataSource : RemoteDataSource {

    override suspend fun getArtist(artistName: String): ArtistDetail? =
        RetrofitBuilder.deezerInstance
            .create(DeezerArtistService::class.java)
            .getArtist(artistName)
            .data
            .firstOrNull()
            ?.toArtistDetail()

    override suspend fun getSongs(artistName: String): List<DomainSong> =
        RetrofitBuilder.deezerInstance
            .create(DeezerArtistService::class.java)
            .getSongs(artistName)
            .data
            .map(SongData::toDomainSong)

    override suspend fun getArtistsByLocation(country: String): List<RecommendedArtist> =
        RetrofitBuilder.musicoveryInstance
            .create(MusicoveryArtistService::class.java)
            .getArtistsByLocation(country.toLowerCase(Locale.ROOT).trim())
            .artists
            .artist
            .map(MusicoveryArtist::toRecommendedArtist)
}