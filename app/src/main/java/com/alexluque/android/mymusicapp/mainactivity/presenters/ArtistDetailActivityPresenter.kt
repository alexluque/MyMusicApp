package com.alexluque.android.mymusicapp.mainactivity.presenters

import SongsData
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.model.controllers.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.ArtistDetailActivityContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistDetailActivityPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: ArtistDetailActivityContract? = null
    private var context: Context? = null

    fun onCreate(contract: ArtistDetailActivityContract, context: Context) {
        initScope()
        this.contract = contract
        this.context = context
    }

    fun onDestroy() {
        cancelScope()
        contract = null
    }

    fun onStartActivityReceived(intent: Intent?, nameView: TextView, imageView: ImageView, dataSet: SongsData, viewAdapter: RecyclerView.Adapter<*>) {
        intent?.let {
            ConnectivityController.runIfConnected {
                val artistName = intent.getStringExtra(ARTIST_NAME)

                launch {
                    val artist = withContext(Dispatchers.IO) {
                        RetrofitBuilder.deezerInstance
                            .create(DeezerArtistService::class.java)
                            .getArtist(artistName!!)
                            .data
                            .firstOrNull()
                    }

                    when (artist != null) {
                        true -> {
                            val songs = withContext(Dispatchers.IO) {
                                RetrofitBuilder.deezerInstance
                                    .create(DeezerArtistService::class.java)
                                    .getSongs(artistName!!)
                            }
                            nameView.text = artist.name
                            imageView.loadImage(artist.picture_big)
                            viewAdapter.updateData(dataSet.data as MutableList<Any>, songs.data)
                        }
                        else -> context?.let { imageView.makeLongSnackbar(context!!.getString(R.string.artist_not_found)) }
                    }

                    contract?.hideProgress()
                }
            }
        }
    }

    companion object {
        const val ARTIST_NAME = "artist name"
    }
}