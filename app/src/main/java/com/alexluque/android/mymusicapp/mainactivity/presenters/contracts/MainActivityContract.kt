package com.alexluque.android.mymusicapp.mainactivity.presenters.contracts

import com.alexluque.android.mymusicapp.mainactivity.presenters.RecommendationsActivityPresenter

interface MainActivityContract {

    fun showRecommendations(countryName: String)
    fun showDefaultRecommendations(countryName: String = RecommendationsActivityPresenter.DEFAULT_COUNTRY)
}