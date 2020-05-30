package com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions

import android.Manifest
import android.app.Activity
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener

fun Activity.addLocationPermission(listener: PermissionListener) =
    Dexter.withActivity(this)
        .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        .withListener(listener)
        .check()

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {

    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProvider(this, vmFactory).get()
}

fun Activity.setStatusBarColor(color: Int) {
    val window: Window = this.window
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this, color)
}