package com.alexluque.android.mymusicapp.mainactivity.presenters

import MusicoveryArtist
import MusicoveryGetByCountryResponse
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.model.network.builders.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationsActivityPresenter {

    fun showRecommendations(viewAdapter: RecyclerView.Adapter<*>, myDataSet: MutableList<MusicoveryArtist>, country: String = DEFAULT_COUNTRY) {
        val service = RetrofitBuilder.musicoveryInstance.create(MusicoveryArtistService::class.java)
        service.getArtistsByLocation(country.toLowerCase().trim()).enqueue(object : Callback<MusicoveryGetByCountryResponse> {

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

    companion object {
        const val DEFAULT_COUNTRY = "usa"
    }
}