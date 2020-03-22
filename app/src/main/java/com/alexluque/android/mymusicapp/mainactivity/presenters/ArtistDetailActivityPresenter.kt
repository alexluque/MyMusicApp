package com.alexluque.android.mymusicapp.mainactivity.presenters

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.model.controllers.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.ArtistDetailActivityContract
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.SearchArtistFragmentContract
import com.alexluque.android.mymusicapp.mainactivity.presenters.objects.ArtistContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST") class ArtistDetailActivityPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: ArtistDetailActivityContract? = null
    private var searchArtistContract: SearchArtistFragmentContract? = null
    private var context: Context? = null

    fun onCreate(contract: ArtistDetailActivityContract, context: Context, searchArtistContract: SearchArtistFragmentContract) {
        initScope()
        this.contract = contract
        this.context = context
        this.searchArtistContract = searchArtistContract
    }

    fun onDestroy() {
        cancelScope()
        contract = null
    }

    fun onStartActivityReceived(intent: Intent? = null, artistName: String? = null, container: ArtistContainer) {
        ConnectivityController.runIfConnected {
            val name = intent?.getStringExtra(ARTIST_NAME) ?: artistName ?: String()

            launch {
                val artist = withContext(Dispatchers.IO) {
                    RetrofitBuilder.deezerInstance
                        .create(DeezerArtistService::class.java)
                        .getArtist(name)
                        .data
                        .firstOrNull()
                }

                when (artist != null) {
                    true -> {
                        val songs = withContext(Dispatchers.IO) {
                            RetrofitBuilder.deezerInstance
                                .create(DeezerArtistService::class.java)
                                .getSongs(name)
                        }
                        container.textView.text = artist.name
                        container.imageView.loadImage(artist.picture_big)
                        container.viewAdapter.updateData(container.dataSet.data as MutableList<Any>, songs.data)
                    }
                    else -> context?.let {
                        val notFoundMsg = context!!.getString(R.string.artist_not_found)
                        container.textView.text = notFoundMsg
                        container.imageView.makeLongSnackbar(notFoundMsg)
                    }
                }

                contract?.hideProgress()
            }
        }
    }

    fun onClickSearchButton(manager: FragmentManager, container: ArtistContainer) =
        searchArtistContract?.onSearchArtistButtonClick(manager, container, this)

    fun updateData(container: ArtistContainer, artistName: String) = onStartActivityReceived(artistName = artistName, container = container)

    companion object {
        const val ARTIST_NAME = "artist name"
    }
}