package com.alexluque.android.mymusicapp.mainactivity.extensions

import android.widget.ImageView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun ImageView.loadImage(url: String) {
    val view = this
    GlobalScope.launch(Dispatchers.Main) {
        Picasso.get()
            .load(url)
            .error(R.drawable.ic_launcher_background)
            .into(view)
    }
}