package com.alexluque.android.mymusicapp.mainactivity.ui.common

import android.app.Application
import com.alexluque.android.mymusicapp.mainactivity.di.DataModule
import com.alexluque.android.mymusicapp.mainactivity.di.MyMusicAppComponent
import com.alexluque.android.data.datasources.LocalDataSource
import com.alexluque.android.data.datasources.RemoteDataSource
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@ExperimentalStdlibApi
@Singleton
@Component(modules = [AppModuleTest::class, DataModule::class])
interface UiTestComponent : MyMusicAppComponent {

    val localDataSource: LocalDataSource
    val remoteDataSource: RemoteDataSource

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): UiTestComponent
    }
}