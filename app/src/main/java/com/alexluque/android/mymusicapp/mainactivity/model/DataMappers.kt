package com.alexluque.android.mymusicapp.mainactivity.model

import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.ArtistData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.MusicoveryArtist
import com.example.android.domain.ArtistDetail
import com.example.android.domain.FavouriteArtist
import com.example.android.domain.RecommendedArtist
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song as RoomSong
import com.example.android.domain.Song as DomainSong


fun ArtistData.toArtistDetail(): ArtistDetail = ArtistDetail(id, name, picture_big, picture_medium)

fun SongData.toDomainSong(): DomainSong = DomainSong(id, title, album?.title)

fun MusicoveryArtist.toRecommendedArtist(): RecommendedArtist = RecommendedArtist(name, genre)

fun Artist.toDomainArtist(): FavouriteArtist = FavouriteArtist(id, name, imageUrl)

fun FavouriteArtist.toRoomArtist(): Artist = Artist(id, name, imageUrl)

fun RoomSong.toDomainSong(): DomainSong = DomainSong(id, title, album, artistId)

fun DomainSong.toRoomSong(): RoomSong = RoomSong(id, title, album, artistId)