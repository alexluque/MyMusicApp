package com.alexluque.android.mymusicapp.mainactivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.hideKeyboard
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.showKeyboard
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.SearchArtistViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.SearchArtistViewModel.UiModel
import kotlinx.android.synthetic.main.fragment_search_artist.view.*
import java.util.*

@SuppressLint("InflateParams")
class SearchArtistFragment(
    private val loadArtistDetail: ((artistName: String) -> Unit)? = null
) : DialogFragment() {

    private val nameEditText: EditText by lazy { mainView.artistName_editText }
    private val mainView: View by lazy {
        this.requireActivity()
            .layoutInflater
            .inflate(R.layout.fragment_search_artist, null)
    }

    private lateinit var viewModel: SearchArtistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchArtistViewModel::class.java)
        viewModel.model.observe(this, Observer(::updateUi))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            createDialog(it)
        } ?: throw IllegalStateException("Activity cannot be null")

        activity?.let { nameEditText.showKeyboard(it) }

        return dialog
    }

    private fun updateUi(model: UiModel) {
        when (model) {
            is UiModel.Create -> dialog
            is UiModel.Search -> {
                activity?.let { nameEditText.hideKeyboard(it) }

                if (loadArtistDetail != null)
                    loadArtistDetail.invoke(retrieveEntry())
                else
                    activity?.myStartActivity(ArtistDetailActivity::class.java, listOf(ARTIST_NAME to retrieveEntry()))
            }
            is UiModel.Cancel -> {
                activity?.let { nameEditText.hideKeyboard(it) }
                dialog?.cancel()
            }
        }
    }

    private fun retrieveEntry() = nameEditText
        .text
        .toString()
        .toLowerCase(Locale.ROOT)
        .trim()

    private fun createDialog(fragmentActivity: FragmentActivity): AlertDialog {
        return AlertDialog.Builder(fragmentActivity)
            .setTitle(fragmentActivity.getString(R.string.artist_dialog_title))
            .setView(mainView)
            .setPositiveButton(fragmentActivity.getString(R.string.search_button)) { _, _ ->
                viewModel.onSearchClicked()
            }
            .setNegativeButton(fragmentActivity.getString(R.string.cancel_button)) { _, _ ->
                viewModel.onCancelClicked()
            }
            .create()
    }

    companion object {
        const val FRAGMENT_NAME = "SearchArtistFragment"
    }
}