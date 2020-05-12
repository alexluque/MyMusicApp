package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.example.android.domain.RecommendedArtist
import kotlinx.android.synthetic.main.recommended_artist.view.*
import retrofit2.Retrofit

class RecommendedArtistsAdapter(
    var artists: MutableList<RecommendedArtist>,
    private val onArtistClicked: (artistName: String) -> Unit,
    private val loadImage: (artistName: String, imageView: ImageView, retrofit: Retrofit) -> Unit
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
        loadImage(artistName, view.recommended_artist_image, RetrofitBuilder.deezerInstance)
        view.recommended_artist_name.text = artistName
        view.recommended_artist_genere.text = data.genre?.toString()
        view.setOnClickListener {
            onArtistClicked(artistName)
        }
    }

    override fun getItemCount() = artists.size
}