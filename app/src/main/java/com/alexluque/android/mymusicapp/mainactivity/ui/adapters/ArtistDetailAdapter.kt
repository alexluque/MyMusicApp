package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import kotlinx.android.synthetic.main.artist_detail.view.*

class ArtistDetailAdapter(
    var songs: List<SongData>,
    private val onFavouriteClicked: (star: ImageView, songId: Long, title: String, album: String?) -> Unit,
    private val isFavourite: (songId: Long) -> Boolean
) : RecyclerView.Adapter<ArtistDetailAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.artist_detail, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val view = holder.view
        val song = songs[position]
        view.song_name.text = song.title
        view.song_album.text = song.album?.title

        if (isFavourite(song.id))
            view.set_favourite.setImageResource(android.R.drawable.btn_star_big_on)

        view.set_favourite.setOnClickListener {
            onFavouriteClicked(
                it as @ParameterName(name = "star") ImageView,
                song.id,
                song.title,
                song.album?.title)
        }
    }

    override fun getItemCount() = songs.size
}