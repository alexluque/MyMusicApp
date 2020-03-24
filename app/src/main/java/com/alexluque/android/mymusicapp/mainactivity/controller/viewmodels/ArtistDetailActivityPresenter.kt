package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.model.objects.ArtistContainer
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getArtist
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getSongs
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.ArtistDetailActivityContract
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.SearchArtistFragmentContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class ArtistDetailActivityPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

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
                    getArtist(name)
                }

                when (artist != null) {
                    true -> {
                        val songs = withContext(Dispatchers.IO) {
                            getSongs(name)
                        }
                        container.textView.text = artist.name
                        container.imageView.loadImage(artist.picture_big)
                        container.viewAdapter.updateData(container.dataSet.data as MutableList<Any>, songs.data)
                    }
                    else -> context?.let {
                        container.imageView.makeLongSnackbar(context!!.getString(R.string.artist_not_found))
                    }
                }
            }
        }
        contract?.hideProgress()
    }

    fun onClickSearchButton(manager: FragmentManager, container: ArtistContainer) =
        searchArtistContract?.onSearchArtistButtonClick(manager, container, this)

    fun updateData(container: ArtistContainer, artistName: String) = onStartActivityReceived(artistName = artistName, container = container)

    companion object {
        const val ARTIST_NAME = "artist name"
    }
}