package com.alexluque.android.mymusicapp.mainactivity.di

import com.alexluque.android.data.datasources.GeolocationDataSource
import com.alexluque.android.data.datasources.LocalDataSource
import com.alexluque.android.data.datasources.RemoteDataSource
import com.alexluque.android.data.repositories.ArtistDetailRepository
import com.alexluque.android.data.repositories.FavouriteArtistsRepository
import com.alexluque.android.data.repositories.GeolocationRepository
import com.alexluque.android.data.repositories.RecommendedArtistsRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun artistDetailRepositoryProvider(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ) = ArtistDetailRepository(remoteDataSource, localDataSource)

    @Provides
    fun favouriteArtistsRepositoryProvider(
        localDataSource: LocalDataSource
    ) = FavouriteArtistsRepository(localDataSource)

    @Provides
    fun geolocationRepositoryProvider(
        geolocationDataSource: GeolocationDataSource
    ) = GeolocationRepository(geolocationDataSource)

    @Provides
    fun recommendedArtistsRepository(
        remoteDataSource: RemoteDataSource
    ) = RecommendedArtistsRepository(remoteDataSource)
}