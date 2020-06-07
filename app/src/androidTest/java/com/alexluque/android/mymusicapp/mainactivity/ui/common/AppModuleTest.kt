package com.alexluque.android.mymusicapp.mainactivity.ui.common

import android.app.Application
import androidx.room.Room
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.network.DeezerMusicoveryDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.GoogleMapsDataSource
import com.alexluque.android.data.datasources.GeolocationDataSource
import com.alexluque.android.data.datasources.LocalDataSource
import com.alexluque.android.data.datasources.RemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModuleTest {

    @Provides
    @Singleton
    fun databaseProvider(app: Application) = Room.databaseBuilder(
        app,
        FavouritesRoomDatabase::class.java,
        "favourites_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun geolocationDataSourceProvider(): GeolocationDataSource = GoogleMapsDataSource()

    @Provides
    @Singleton
    fun remoteDataSource(): RemoteDataSource = DeezerMusicoveryDataSource()

    @Provides
    @Singleton
    fun localDataSource(
        favouritesRoomDatabase: FavouritesRoomDatabase
    ): LocalDataSource = RoomDataSourceTest(favouritesRoomDatabase)
}

