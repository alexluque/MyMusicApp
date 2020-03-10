package com.example.android.mymusicapp.mainactivity.presenters

import MusicoveryArtist
import MusicoveryGetByCountryResponse
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.example.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationsActivityPresenter {

    fun showRandomRecommendations(viewAdapter: RecyclerView.Adapter<*>, myDataSet: MutableList<MusicoveryArtist>) {
        GlobalScope.launch(Dispatchers.IO) {
            val service = RetrofitBuilder.musicoveryInstance.create(MusicoveryArtistService::class.java)
            service.getArtistsByLocation("america").enqueue(object : Callback<MusicoveryGetByCountryResponse> {

                override fun onFailure(call: Call<MusicoveryGetByCountryResponse>, t: Throwable) {
                    Log.e(this@RecommendationsActivityPresenter.javaClass.name, "Error while retrieving recommended artists from Musicovery's API...")
                }

                override fun onResponse(call: Call<MusicoveryGetByCountryResponse>, response: Response<MusicoveryGetByCountryResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            myDataSet.clear()
                            myDataSet.addAll(it.artists.artist)
                            viewAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
        }
    }
}