package com.example.android.mymusicapp.mainactivity.ui.adapters

import MusicoveryArtist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymusicapp.mainactivity.R
import com.example.android.mymusicapp.mainactivity.extensions.loadImage
import kotlinx.android.synthetic.main.recommended_artist.view.*

class RecommendedArtistsAdapter(private val myDataSet: MutableList<MusicoveryArtist>) :
    RecyclerView.Adapter<RecommendedArtistsAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommended_artist, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = holder.view
        val data = myDataSet[position]
        view.recommended_artist_image.loadImage("https://picsum.photos/200/300") // TODO: load real image from artist
        view.recommended_artist_name.text = data.name
        view.recommended_artist_genere.text = data.genre
    }

    override fun getItemCount() = myDataSet.size
}