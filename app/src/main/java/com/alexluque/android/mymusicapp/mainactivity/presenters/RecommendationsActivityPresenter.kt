package com.alexluque.android.mymusicapp.mainactivity.presenters

import MusicoveryArtist
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.model.controllers.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.RecommendationsActivityContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationsActivityPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: RecommendationsActivityContract? = null
    private var context: Context? = null

    fun onCreate(contract: RecommendationsActivityContract, context: Context) {
        initScope()
        this.contract = contract
        this.context = context
    }

    fun onDestroy() {
        cancelScope()
        contract = null
    }

    fun showRecommendations(viewAdapter: RecyclerView.Adapter<*>, myDataSet: MutableList<MusicoveryArtist>, country: String) {
        ConnectivityController.runIfConnected {
            launch {
                val artists = withContext(Dispatchers.IO) {
                    RetrofitBuilder.musicoveryInstance
                        .create(MusicoveryArtistService::class.java)
                        .getArtistsByLocation(country.toLowerCase().trim())
                }
                myDataSet.clear()
                myDataSet.addAll(artists.artists.artist)
                viewAdapter.notifyDataSetChanged()
                contract?.hideProgress()
                contract?.makeSnackbar(context?.getString(R.string.country_recommendations) + " ${country.toUpperCase()}")
            }
        }
    }

    companion object {
        const val DEFAULT_COUNTRY = "usa"
    }
}