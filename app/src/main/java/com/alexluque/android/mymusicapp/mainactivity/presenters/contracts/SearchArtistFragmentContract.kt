package com.alexluque.android.mymusicapp.mainactivity.presenters.contracts

import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.presenters.ArtistDetailActivityPresenter
import com.alexluque.android.mymusicapp.mainactivity.presenters.objects.ArtistContainer

interface SearchArtistFragmentContract {

    fun onSearchArtistButtonClick(manager: FragmentManager, container: ArtistContainer? = null, detailPresenter: ArtistDetailActivityPresenter? = null)
    fun retrieveEntry(): String
}