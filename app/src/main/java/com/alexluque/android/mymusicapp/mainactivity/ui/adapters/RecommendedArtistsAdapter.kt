package com.alexluque.android.mymusicapp.mainactivity.ui.adapters

import MusicoveryArtist
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailActivityPresenter
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getArtist
import kotlinx.android.synthetic.main.recommended_artist.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecommendedArtistsAdapter(var myDataSet: MutableList<MusicoveryArtist>, private val context: Context) :
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
        val artistName = data.name
        loadImage(artistName, view.recommended_artist_image)
        view.recommended_artist_name.text = artistName
        view.recommended_artist_genere.text = data.genre
        view.setOnClickListener {
            context.myStartActivity(ArtistDetailActivity::class.java, listOf(ArtistDetailActivityPresenter.ARTIST_NAME to artistName))
        }
    }

    override fun getItemCount() = myDataSet.size

    private fun loadImage(artistName: String, imageView: ImageView) {
        ConnectivityController.runIfConnected {
            GlobalScope.launch(Dispatchers.IO) {
                val artist = getArtist(artistName)
                imageView.loadImage(artist?.picture_medium ?: RANDOM_IMAGE)
            }
        }
    }

    private companion object {
        private const val RANDOM_IMAGE = "https://picsum.photos/200/300"
    }
}