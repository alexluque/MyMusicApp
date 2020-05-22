package com.alexluque.android.mymusicapp.mainactivity.ui.common

import com.alexluque.android.mymusicapp.mainactivity.MyMusicApp
import com.alexluque.android.mymusicapp.mainactivity.di.MyMusicAppComponent

@ExperimentalStdlibApi
class TestApplication: MyMusicApp() {

    override fun initMusicAppComponent(): MyMusicAppComponent =
        DaggerUiTestComponent.factory().create(this)
}