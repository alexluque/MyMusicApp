package com.alexluque.android.mymusicapp.mainactivity

import android.app.Application
import com.alexluque.android.mymusicapp.mainactivity.di.DaggerMyMusicAppComponent
import com.alexluque.android.mymusicapp.mainactivity.di.MyMusicAppComponent

class MyMusicApp : Application() {

    lateinit var component: MyMusicAppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerMyMusicAppComponent
            .factory()
            .create(this)
    }
}