package com.alexluque.android.mymusicapp.mainactivity.ui

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.loadImage

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    visibility = visible?.let {
        if (visible) View.VISIBLE else View.GONE
    } ?: View.GONE
}

@BindingAdapter("image")
fun ImageView.loadImage(url: String?) = url?.let { this.loadImage(url) }

@BindingAdapter("toolbarTitle")
fun Toolbar.setToolbarTitle(title: String?) = title?.let { this.title = title }