package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import SongsData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import kotlinx.android.synthetic.main.artist_detail.view.*

class ArtistDetailAdapter(private val myDataSet: SongsData) :
    RecyclerView.Adapter<ArtistDetailAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.artist_detail, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = holder.view
        val data = myDataSet.data[position]
        view.song_name.text = data.title
        view.song_album.text = data.albumData?.title
    }

    override fun getItemCount() = myDataSet.data.size
}