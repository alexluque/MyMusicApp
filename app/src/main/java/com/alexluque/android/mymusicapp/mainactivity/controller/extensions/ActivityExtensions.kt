package com.alexluque.android.mymusicapp.mainactivity.controller.extensions

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener

fun Activity.addLocationPermission(listener: PermissionListener) =
    Dexter.withActivity(this)
        .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        .withListener(listener)
        .check()