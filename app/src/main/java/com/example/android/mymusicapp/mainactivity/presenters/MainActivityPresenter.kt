package com.example.android.mymusicapp.mainactivity.presenters

import DeezerResponse
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymusicapp.mainactivity.RecommendationsActivity
import com.example.android.mymusicapp.mainactivity.network.builders.RetrofitBuilder
import com.example.android.mymusicapp.mainactivity.network.services.DeezerArtistService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityPresenter {

    fun getSongs(artist: String, viewAdapter: RecyclerView.Adapter<*>, myDataSet: MutableList<Any>) {
        GlobalScope.launch(Dispatchers.IO) {
            val service = RetrofitBuilder.deezerInstance.create(DeezerArtistService::class.java)
            service.getSongs(artist).enqueue(object : Callback<DeezerResponse> {

                override fun onFailure(call: Call<DeezerResponse>, t: Throwable) {
                    Log.e(this@MainActivityPresenter.javaClass.name, "Error while retrieving songs from Deezer's API...")
                }

                override fun onResponse(call: Call<DeezerResponse>, response: Response<DeezerResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            myDataSet.clear()
                            myDataSet.addAll(it.data)
                            viewAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
        }
    }

    fun startRecommendationsActivity(context: Context) {
        val intent = Intent(context, RecommendationsActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, RecommendationsActivity.RECOMMEND)
        }
        startActivity(context, intent, null)
    }
}