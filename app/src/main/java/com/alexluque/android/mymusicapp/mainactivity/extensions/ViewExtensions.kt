package com.alexluque.android.mymusicapp.mainactivity.extensions

import android.view.View
import android.widget.ImageView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.google.android.material.snackbar.Snackbar
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

fun View.makeLongSnackbar(msg: String) = Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()