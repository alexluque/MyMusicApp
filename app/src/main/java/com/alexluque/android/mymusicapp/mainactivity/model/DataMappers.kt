package com.alexluque.android.mymusicapp.mainactivity.model

import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.ArtistData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.ArtistInfo
import com.example.android.domain.ArtistDetail
import com.example.android.domain.FavouriteArtist
import com.example.android.domain.Genre
import com.example.android.domain.RecommendedArtist
import com.google.gson.internal.LinkedTreeMap
import java.util.*
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist as RoomArtist
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song as RoomSong
import com.example.android.domain.Artist as DomainArtist
import com.example.android.domain.ArtistInfo as DomainArtistInfo
import com.example.android.domain.Song as DomainSong


fun ArtistData.toArtistDetail(): ArtistDetail = ArtistDetail(id, name, picture_big, picture_medium)

fun SongData.toDomainSong(): DomainSong = DomainSong(id, title, album?.title)

fun Artist.toRecommendedArtist(): RecommendedArtist = RecommendedArtist(name, genre?.toString())

fun RoomArtist.toDomainArtist(): FavouriteArtist {
    val artist = FavouriteArtist(id, name, imageUrl)
    artist.genre = genre
    artist.regionAndCountry = regionAndCountry
    return artist
}

fun FavouriteArtist.toRoomArtist(): RoomArtist = RoomArtist(id, name, imageUrl, genre, regionAndCountry)

fun RoomSong.toDomainSong(): DomainSong = DomainSong(id, title, album, artistId)

fun DomainSong.toRoomSong(): RoomSong = RoomSong(id, title, album, artistId)

fun Artist.toDomainArtist(): DomainArtist = DomainArtist(mbid, name, genre?.toString(), country)

fun LinkedTreeMap<String, String>.toDomainArtist(): DomainArtist {
    val values = this.values
    val mbid = values.elementAt(0) ?: String()
    val name = values.elementAt(1) ?: String()
    val genre = try {
        values.elementAt(2).toString()
    } catch (ex: ClassCastException) {
        String()
    }
    val country = try {
        values.elementAt(3).toString()
    } catch (ex: ClassCastException) {
        String()
    }
    return DomainArtist(mbid, name, genre, country)
}

fun emptyDomainArtist(): DomainArtist = DomainArtist(String(), String(), String(), String())

@ExperimentalStdlibApi
fun ArtistInfo.toDomainArtistInfo(): DomainArtistInfo {
    val domainGenres = if (genres != null) {
        when (genres) {
            is List<*> -> {
                if (genres.isNotEmpty())
                    genres.take(2).joinToString { it.toString().capitalize(Locale.ROOT) }
                else
                    String()
            }
            is Genre -> if (genres.genre != null) genres.genre else String()
            is Map<*, *> -> {
                if (genres.isNotEmpty())
                    genres.entries
                        .map { it.value.toString().capitalize(Locale.ROOT) }[0]
                        .split(delimiters = *arrayOf(", "))
                        .map { it.replace("[", String()) }
                        .map { it.replace("]", String()) }
                        .take(2)
                        .joinToString { it.capitalize(Locale.ROOT) }
                else
                    String()
            }
            else -> genres
        }
    } else {
        String()
    }
    return DomainArtistInfo(name, mbid, domainGenres, country, region?.toString())
}