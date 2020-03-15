package com.alexluque.android.mymusicapp.mainactivity.presenters

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.RecommendationsActivity
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.MainActivityContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityPresenter : MyCoroutineScope by MyCoroutineScope.Implementation() {

    private var contract: MainActivityContract? = null

    fun onCreate(contract: MainActivityContract) {
        initScope()
        this.contract = contract
    }

    fun onDestroy() {
        cancelScope()
        this.contract = null
    }

    fun getSongs(artist: String, viewAdapter: RecyclerView.Adapter<*>, myDataSet: MutableList<Any>) {
        launch {
            val songs = withContext(Dispatchers.IO) {
                RetrofitBuilder.deezerInstance
                    .create(DeezerArtistService::class.java)
                    .getSongs(artist)
            }
            myDataSet.clear()
            myDataSet.addAll(songs.data)
            viewAdapter.notifyDataSetChanged()
        }
    }

    fun onRecommendationsClicked(context: Context, countryName: String) {
        val intent = Intent(context, RecommendationsActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, countryName)
        }
        startActivity(context, intent, null)
    }
}