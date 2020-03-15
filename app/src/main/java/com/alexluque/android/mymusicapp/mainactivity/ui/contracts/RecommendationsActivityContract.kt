package com.alexluque.android.mymusicapp.mainactivity.ui.contracts

interface RecommendationsActivityContract {

    fun showRecommendations()
    fun hideProgress()
    fun makeSnackbar(msg: String)
}