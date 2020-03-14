package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import ArtistData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.extensions.loadImage
import kotlinx.android.synthetic.main.favourite_artist.view.*

class FavouriteArtistsAdapter(private val myDataSet: MutableList<ArtistData>) :
    RecyclerView.Adapter<FavouriteArtistsAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_artist, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = holder.view
        val data = myDataSet[position]
        view.artist_avatar.loadImage(data.picture_medium)
        view.artist_name.text = data.name
//        view.artist_fav_num.text = TODO: grab amount of existing favourite songs from this artist
    }

    override fun getItemCount() = myDataSet.size
}