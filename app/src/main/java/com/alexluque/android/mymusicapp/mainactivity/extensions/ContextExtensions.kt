package com.alexluque.android.mymusicapp.mainactivity.extensions

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

fun Context.myStartActivity(className: Class<*>, extras: List<Pair<String, String>>?) =
    ContextCompat.startActivity(this, Intent(this, className).apply {
        extras?.let {
            extras.forEach { putExtra(it.first, it.second) }
        }
    }, null)