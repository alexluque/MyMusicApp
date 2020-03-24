package com.alexluque.android.mymusicapp.mainactivity.model.objects

import SongsData
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ArtistContainer (
    val textView: TextView,
    val imageView: ImageView,
    val dataSet: SongsData,
    val viewAdapter: RecyclerView.Adapter<*>
)