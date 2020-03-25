package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import ArtistData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import kotlinx.android.synthetic.main.favourite_artist.view.*

class FavouriteArtistsAdapter(
    var artists: List<ArtistData>,
    private val onArtistClicked: (artistName: String) -> Unit
) : RecyclerView.Adapter<FavouriteArtistsAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_artist, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = holder.view
        val data = artists[position]
        val artistName = data.name
        view.artist_avatar.loadImage(data.picture_medium)
        view.artist_name.text = artistName
//        view.artist_fav_num.text = TODO: number of fav songs in DB from this artist
        view.setOnClickListener {
            onArtistClicked(artistName)
        }
    }

    override fun getItemCount() = artists.size
}