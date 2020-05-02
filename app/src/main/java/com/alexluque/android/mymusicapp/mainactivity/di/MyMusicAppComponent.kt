package com.alexluque.android.mymusicapp.mainactivity.di

import android.app.Application
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.DetailActivityComponent
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.DetailActivityModule
import com.alexluque.android.mymusicapp.mainactivity.ui.main.MainActivityComponent
import com.alexluque.android.mymusicapp.mainactivity.ui.main.MainActivityModule
import com.alexluque.android.mymusicapp.mainactivity.ui.recommendations.RecommendationsActivityComponent
import com.alexluque.android.mymusicapp.mainactivity.ui.recommendations.RecommendationsActivityModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface MyMusicAppComponent {

    fun plus(mainModule: MainActivityModule): MainActivityComponent
    fun plus(module: DetailActivityModule): DetailActivityComponent
    fun plus(module: RecommendationsActivityModule): RecommendationsActivityComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): MyMusicAppComponent
    }
}