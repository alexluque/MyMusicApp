package com.alexluque.android.mymusicapp.mainactivity

import ArtistData
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.presenters.SearchArtistFragmentPresenter
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.SearchArtistFragmentContract
import kotlinx.android.synthetic.main.fragment_search_artist.*
import java.util.*

class SearchArtistFragment : DialogFragment(), SearchArtistFragmentContract {

    private val presenter: SearchArtistFragmentPresenter by lazy { SearchArtistFragmentPresenter() }

    private lateinit var dialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = presenter.onCreateDialog(this, activity, this)
        return dialog
    }

    override fun onSearchArtistButtonClick(supportFragmentManager: FragmentManager) = presenter.onSearchArtistButtonClick(supportFragmentManager)

    override fun retrieveEntry(): String = dialog.artistName_editText.text.toString().toLowerCase(Locale.ROOT).trim()

}
