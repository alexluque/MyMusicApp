package com.alexluque.android.mymusicapp.mainactivity.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.example.android.domain.FavouriteArtist
import kotlinx.android.synthetic.main.favourite_artist_card.view.*

class FavouriteArtistsAdapter(
    var artists: MutableList<FavouriteArtist>,
    private val onArtistClicked: (artistName: String) -> Unit
) : RecyclerView.Adapter<FavouriteArtistsAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_artist_card, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = holder.view
        val artist = artists[position]
        val artistName = artist.name
        view.artist_avatar.loadImage(artist.imageUrl)
        view.artist_name.text = artistName
        view.artist_fav_num.text = artist.favouriteSongs.toString()
        view.artist_genre.text = artist.genre

        artist.genre?.let {
            if (it.isNotEmpty())
                view.artist_genre.visibility = View.VISIBLE
        }

        view.artist_region_and_country.text = artist.regionAndCountry
        view.setOnClickListener { onArtistClicked(artistName) }
    }

    override fun getItemCount() = artists.size
}