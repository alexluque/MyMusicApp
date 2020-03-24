package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.hideKeyboard
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.showKeyboard
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailActivityPresenter.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.SearchArtistFragmentContract
import com.alexluque.android.mymusicapp.mainactivity.model.objects.ArtistContainer
import kotlinx.android.synthetic.main.fragment_search_artist.view.*

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
            val view = fragment.requireActivity()
                .layoutInflater
                .inflate(R.layout.fragment_search_artist, null)
            view.artistName_editText.showKeyboard(activity)

            AlertDialog.Builder(it)
                .setTitle(activity.getString(R.string.artist_dialog_title))
                .setView(view)
                .setPositiveButton(activity.getString(R.string.search_button)) { _, _ ->
                    view.artistName_editText.hideKeyboard(activity)
                    val artistName = contract.retrieveEntry()

                    when (artistContainer != null && detailPresenter != null) {
                        true -> detailPresenter.updateData(artistContainer, artistName)
                        else -> activity.myStartActivity(ArtistDetailActivity::class.java, listOf(ARTIST_NAME to artistName))
                    }
                }
                .setNegativeButton(activity.getString(R.string.cancel_button)) { dialog, _ ->
                    view.artistName_editText.hideKeyboard(activity)
                    dialog.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val FRAGMENT_NAME = "SearchArtistFragment"
    }
}