package com.alexluque.android.mymusicapp.mainactivity

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.presenters.ArtistDetailActivityPresenter
import com.alexluque.android.mymusicapp.mainactivity.presenters.SearchArtistFragmentPresenter
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.SearchArtistFragmentContract
import com.alexluque.android.mymusicapp.mainactivity.presenters.objects.ArtistContainer
import kotlinx.android.synthetic.main.fragment_search_artist.*
import java.util.*

class SearchArtistFragment(
    private val artistContainer: ArtistContainer? = null,
    private val detailPresenter: ArtistDetailActivityPresenter? = null
) : DialogFragment(), SearchArtistFragmentContract {

    private val presenter: SearchArtistFragmentPresenter by lazy { SearchArtistFragmentPresenter() }

    private lateinit var dialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = presenter.onCreateDialog(this, activity, this, artistContainer, detailPresenter)
        return dialog
    }

    override fun onSearchArtistButtonClick(
        manager: FragmentManager,
        container: ArtistContainer?,
        detailPresenter: ArtistDetailActivityPresenter?
    ) = presenter.onSearchArtistButtonClick(manager, container, detailPresenter)

    override fun retrieveEntry(): String =
        dialog.artistName_editText
            .text
            .toString()
            .toLowerCase(Locale.ROOT)
            .trim()
}
