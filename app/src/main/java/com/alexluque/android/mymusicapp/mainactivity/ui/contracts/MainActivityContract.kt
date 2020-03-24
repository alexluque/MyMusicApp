package com.alexluque.android.mymusicapp.mainactivity.ui.contracts

import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModel

interface MainActivityContract {

    fun showRecommendations(countryName: String)
    fun showDefaultRecommendations(countryName: String = RecommendationsViewModel.DEFAULT_COUNTRY)
}