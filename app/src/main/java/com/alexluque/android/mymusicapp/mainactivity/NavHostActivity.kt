package com.alexluque.android.mymusicapp.mainactivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController

class NavHostActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_host)
        ConnectivityController.registerCallback(this, mainView)
    }
}
