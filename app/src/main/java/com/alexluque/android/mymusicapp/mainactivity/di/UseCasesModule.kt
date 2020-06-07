package com.alexluque.android.mymusicapp.mainactivity.di

import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.usecases.*
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun deleteArtistProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = DeleteArtist(artistDetailRepository)

    @Provides
    fun deleteSongProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = DeleteSong(artistDetailRepository)

    @Provides
    fun favouriteArtistHasSongsProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = FavouriteArtistHasSongs(artistDetailRepository)

    @Provides
    fun getArtistProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = GetArtist(artistDetailRepository)

    @Provides
    fun getArtistDetailProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = GetArtist(artistDetailRepository)

    @Provides
    fun getArtistInfoProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = GetArtistInfo(artistDetailRepository)

    @Provides
    fun getFavouriteSongsProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = GetFavouriteSongs(artistDetailRepository)

    @Provides
    fun getSongsProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = GetSongs(artistDetailRepository)

    @Provides
    fun insertArtistProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = InsertArtist(artistDetailRepository)

    @Provides
    fun insertSongProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = InsertSong(artistDetailRepository)

    @Provides
    fun isFavouriteArtistProvider(
        artistDetailRepository: ArtistDetailRepository
    ) = IsFavouriteArtist(artistDetailRepository)
}