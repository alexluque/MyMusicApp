package com.example.android.mymusicapp.mainactivity.presenters

import Data
import DeezerResponse
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymusicapp.mainactivity.MainActivity
import com.example.android.mymusicapp.mainactivity.network.RetrofitBuilder
import com.example.android.mymusicapp.mainactivity.network.services.DeezerArtistService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityPresenter(private val activity: MainActivity) {

    val retrofit = RetrofitBuilder.instance

    fun getSongs(artist: String, viewAdapter: RecyclerView.Adapter<*>, myDataset: MutableList<Data>) {
        val service = retrofit.create(DeezerArtistService::class.java)
        service.getSongs(artist).enqueue(object : Callback<DeezerResponse>{

            override fun onFailure(call: Call<DeezerResponse>, t: Throwable) {}

            override fun onResponse(call: Call<DeezerResponse>, response: Response<DeezerResponse>) {
                if (response.isSuccessful) {
                    myDataset.clear()
                    myDataset.addAll(response.body()!!.data)
                    viewAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}