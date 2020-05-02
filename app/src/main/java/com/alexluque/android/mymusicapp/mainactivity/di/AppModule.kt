package com.alexluque.android.mymusicapp.mainactivity.di

import android.app.Application
import androidx.room.Room
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.RoomDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.DeezerMusicoveryDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.GoogleMapsDataSource
import com.example.android.data.datasources.GeolocationDataSource
import com.example.android.data.datasources.LocalDataSource
import com.example.android.data.datasources.RemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application) = Room.databaseBuilder(
        app,
        FavouritesRoomDatabase::class.java,
        "favourites_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun geolocationDataSourceProvider(): GeolocationDataSource = GoogleMapsDataSource()

    @Provides
    fun remoteDataSource(): RemoteDataSource = DeezerMusicoveryDataSource()

    @Provides
    fun localDataSource(
        favouritesRoomDatabase: FavouritesRoomDatabase
    ): LocalDataSource = RoomDataSource(favouritesRoomDatabase)
}