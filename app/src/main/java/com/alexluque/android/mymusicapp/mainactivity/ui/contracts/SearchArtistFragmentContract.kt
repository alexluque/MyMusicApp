package com.alexluque.android.mymusicapp.mainactivity.ui.contracts

import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailActivityPresenter
import com.alexluque.android.mymusicapp.mainactivity.model.objects.ArtistContainer

interface SearchArtistFragmentContract {

    fun onSearchArtistButtonClick(manager: FragmentManager, container: ArtistContainer? = null, detailPresenter: ArtistDetailActivityPresenter? = null)
    fun retrieveEntry(): String
}