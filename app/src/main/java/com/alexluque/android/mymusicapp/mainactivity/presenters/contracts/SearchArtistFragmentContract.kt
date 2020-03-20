package com.alexluque.android.mymusicapp.mainactivity.presenters.contracts

import androidx.fragment.app.FragmentManager

interface SearchArtistFragmentContract {

    fun onSearchArtistButtonClick(supportFragmentManager: FragmentManager)
    fun retrieveEntry(): String
}