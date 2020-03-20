package com.alexluque.android.mymusicapp.mainactivity.presenters

import ArtistData
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.RecommendationsActivity
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

    fun onRecommendationsClicked(context: Context, countryName: String) {
        val intent = Intent(context, RecommendationsActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, countryName)
        }
        startActivity(context, intent, null)
    }

    fun onClickAddButton(supportFragmentManager: FragmentManager) {
        searchArtistContract?.onSearchArtistButtonClick(supportFragmentManager)
    }
}