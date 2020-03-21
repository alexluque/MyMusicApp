package com.alexluque.android.mymusicapp.mainactivity.presenters

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment
import com.alexluque.android.mymusicapp.mainactivity.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.presenters.ArtistDetailActivityPresenter.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.SearchArtistFragmentContract
import com.alexluque.android.mymusicapp.mainactivity.presenters.objects.ArtistContainer

class SearchArtistFragmentPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: SearchArtistFragmentContract? = null

    fun onSearchArtistButtonClick(
        supportFragmentManager: FragmentManager,
        artistContainer: ArtistContainer? = null,
        detailPresenter: ArtistDetailActivityPresenter? = null
    ) = SearchArtistFragment(artistContainer, detailPresenter).show(supportFragmentManager, FRAGMENT_NAME)

    fun onCreateDialog(
        contract: SearchArtistFragmentContract,
        activity: FragmentActivity?,
        fragment: Fragment,
        artistContainer: ArtistContainer? = null,
        detailPresenter: ArtistDetailActivityPresenter? = null
    ): AlertDialog {
        this.contract = contract

        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(activity.getString(R.string.artist_dialog_title))
                .setView(fragment.requireActivity().layoutInflater
                    .inflate(R.layout.fragment_search_artist, null))
                .setPositiveButton(activity.getString(R.string.search_button)) { _, _ ->
                    val artistName = contract.retrieveEntry()

                    when (artistContainer != null && detailPresenter != null) {
                        true -> detailPresenter.updateData(artistContainer, artistName)
                        else -> activity.myStartActivity(ArtistDetailActivity::class.java, listOf(ARTIST_NAME to artistName))
                    }
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