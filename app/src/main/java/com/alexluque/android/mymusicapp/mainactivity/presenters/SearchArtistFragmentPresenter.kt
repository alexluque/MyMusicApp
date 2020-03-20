package com.alexluque.android.mymusicapp.mainactivity.presenters

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment
import com.alexluque.android.mymusicapp.mainactivity.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.model.controllers.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService
import com.alexluque.android.mymusicapp.mainactivity.presenters.ArtistDetailActivityPresenter.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.SearchArtistFragmentContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchArtistFragmentPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: SearchArtistFragmentContract? = null

    fun getSongs(artist: String, viewAdapter: RecyclerView.Adapter<*>, myDataSet: MutableList<Any>) {
        ConnectivityController.runIfConnected {
            launch {
                val songs = withContext(Dispatchers.IO) {
                    RetrofitBuilder.deezerInstance
                        .create(DeezerArtistService::class.java)
                        .getSongs(artist)
                }
                viewAdapter.updateData(myDataSet, songs.data)
            }
        }
    }

    fun onSearchArtistButtonClick(supportFragmentManager: FragmentManager) = SearchArtistFragment().show(supportFragmentManager, FRAGMENT_NAME)

    fun onCreateDialog(contract: SearchArtistFragmentContract, activity: FragmentActivity?, fragment: Fragment): AlertDialog {
        this.contract = contract

        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(activity.getString(R.string.artist_dialog_title))
                .setView(fragment.requireActivity().layoutInflater
                    .inflate(R.layout.fragment_search_artist, null))
                .setPositiveButton(activity.getString(R.string.search_button)) { _, _ ->
                    val artistName = contract.retrieveEntry()
                    activity.myStartActivity(ArtistDetailActivity::class.java, listOf(ARTIST_NAME to artistName))
                }
                .setNegativeButton(activity.getString(R.string.cancel_button)) { dialog, _ ->
                    dialog.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val FRAGMENT_NAME = "SearchArtistFragment"
    }
}