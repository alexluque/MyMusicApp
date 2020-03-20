package com.alexluque.android.mymusicapp.mainactivity.presenters.contracts

interface RecommendationsActivityContract {

    fun showRecommendations()
    fun hideProgress()
    fun makeSnackbar(msg: String)
}