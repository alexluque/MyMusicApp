package com.alexluque.android.mymusicapp.mainactivity.ui.common

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner

@ExperimentalStdlibApi
class UiRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        )
        super.onCreate(arguments)
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}