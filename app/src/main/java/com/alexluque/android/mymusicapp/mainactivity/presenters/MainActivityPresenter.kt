package com.alexluque.android.mymusicapp.mainactivity.presenters

import android.content.Context
import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.fragment.app.FragmentManager
import com.alexluque.android.mymusicapp.mainactivity.RecommendationsActivity
import com.alexluque.android.mymusicapp.mainactivity.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.MainActivityContract
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.SearchArtistFragmentContract

class MainActivityPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: MainActivityContract? = null
    private var searchArtistContract: SearchArtistFragmentContract? = null

    fun onCreate(contract: MainActivityContract, searchArtistContract: SearchArtistFragmentContract) {
        initScope()
        this.contract = contract
        this.searchArtistContract = searchArtistContract
    }

    fun onDestroy() {
        cancelScope()
        this.contract = null
    }

    fun onRecommendationsClicked(context: Context, countryName: String) =
        context.myStartActivity(RecommendationsActivity::class.java, listOf(EXTRA_MESSAGE to countryName))


    fun onClickAddButton(supportFragmentManager: FragmentManager) = searchArtistContract?.onSearchArtistButtonClick(supportFragmentManager)
}