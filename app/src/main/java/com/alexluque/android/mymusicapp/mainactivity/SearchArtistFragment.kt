package com.alexluque.android.mymusicapp.mainactivity

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.SearchArtistFragmentPresenter
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.SearchArtistFragmentContract
import kotlinx.android.synthetic.main.fragment_search_artist.*
import java.util.*

class SearchArtistFragment(
    private val loadArtistDetail: ((artistName: String) -> Unit)? = null
) : DialogFragment(), SearchArtistFragmentContract {

    private val presenter: SearchArtistFragmentPresenter by lazy { SearchArtistFragmentPresenter() }

    private lateinit var dialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = presenter.onCreateDialog(this, activity, this, loadArtistDetail)
        return dialog
    }

    override fun retrieveEntry(): String =
        dialog.artistName_editText
            .text
            .toString()
            .toLowerCase(Locale.ROOT)
            .trim()

    companion object {
        const val FRAGMENT_NAME = "SearchArtistFragment"
    }
}
