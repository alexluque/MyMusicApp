package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import MusicoveryArtist
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.presenters.ArtistDetailActivityPresenter
import kotlinx.android.synthetic.main.recommended_artist.view.*

class RecommendedArtistsAdapter(private val myDataSet: MutableList<MusicoveryArtist>, private val context: Context) :
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
        val artistName = data.name
        view.recommended_artist_name.text = artistName
        view.recommended_artist_genere.text = data.genre
        view.setOnClickListener {
            context.myStartActivity(ArtistDetailActivity::class.java, listOf(ArtistDetailActivityPresenter.ARTIST_NAME to artistName))
        }
    }

    override fun getItemCount() = myDataSet.size
}