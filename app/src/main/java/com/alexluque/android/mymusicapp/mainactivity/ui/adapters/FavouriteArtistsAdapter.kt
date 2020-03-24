package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import ArtistData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailActivityPresenter.Companion.ARTIST_NAME
import kotlinx.android.synthetic.main.favourite_artist.view.*

class FavouriteArtistsAdapter(private val myDataSet: MutableList<ArtistData>, private val context: Context) :
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
        val artistName = data.name
        view.artist_name.text = artistName
//        view.artist_fav_num.text = TODO: number of fav songs in DB from this artist
        view.setOnClickListener {
            context.myStartActivity(ArtistDetailActivity::class.java, listOf(ARTIST_NAME to artistName))
        }
    }

    override fun getItemCount() = myDataSet.size
}