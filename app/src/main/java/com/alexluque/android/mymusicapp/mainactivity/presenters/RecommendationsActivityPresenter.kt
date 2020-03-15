package com.alexluque.android.mymusicapp.mainactivity.presenters

import MusicoveryArtist
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.RecommendationsActivityContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationsActivityPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: RecommendationsActivityContract? = null

    fun onCreate(contract: RecommendationsActivityContract) {
        initScope()
        this.contract = contract
    }

    fun onDestroy() {
        cancelScope()
        contract = null
    }

    fun showRecommendations(viewAdapter: RecyclerView.Adapter<*>, myDataSet: MutableList<MusicoveryArtist>, country: String) {
        launch {
            val artists = withContext(Dispatchers.IO) {
                RetrofitBuilder.musicoveryInstance
                    .create(MusicoveryArtistService::class.java)
                    .getArtistsByLocation(country.toLowerCase().trim())
            }
            myDataSet.clear()
            myDataSet.addAll(artists.artists.artist)
            viewAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val DEFAULT_COUNTRY = "usa"
    }
}