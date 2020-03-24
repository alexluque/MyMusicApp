package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import MusicoveryArtist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import kotlinx.android.synthetic.main.recommended_artist.view.*

class RecommendedArtistsAdapter(
    var artists: List<MusicoveryArtist>,
    private val onArtistClicked: (artistName: String) -> Unit,
    private val loadImage: (artistName: String, imageView: ImageView) -> Unit
) : RecyclerView.Adapter<RecommendedArtistsAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommended_artist, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = holder.view
        val data = artists[position]
        val artistName = data.name
        loadImage(artistName, view.recommended_artist_image)
        view.recommended_artist_name.text = artistName
        view.recommended_artist_genere.text = data.genre
        view.setOnClickListener {
            onArtistClicked(artistName)
        }
    }

    override fun getItemCount() = artists.size
}