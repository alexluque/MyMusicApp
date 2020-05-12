package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.common.Event
import com.alexluque.android.mymusicapp.mainactivity.ui.common.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.example.android.domain.RecommendedArtist
import com.example.android.usecases.GetArtistDetail
import com.example.android.usecases.GetCountry
import com.example.android.usecases.GetRecommendedArtists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModel(
    latitude: String,
    longitude: String,
    mapsKey: String,
    private val getArtistDetail: GetArtistDetail,
    private val getRecommendedArtists: GetRecommendedArtists,
    private val getCountry: GetCountry
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _artists = MutableLiveData<List<RecommendedArtist>>()
    val artists: LiveData<List<RecommendedArtist>> get() = _artists

    private val _detail = MutableLiveData<Event<String>>()
    val detail: LiveData<Event<String>> get() = _detail

    private val _country = MutableLiveData<Event<String>>()
    val country: LiveData<Event<String>> get() = _country

    init {
        initScope()
        retrieveCountry(RetrofitBuilder.geocodingInstance, latitude, longitude, mapsKey)
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun loadImage(artistName: String, imageView: ImageView, retrofit: Retrofit) {
        ConnectivityController.runIfConnected {
            launch {
                val artist = withContext(Dispatchers.IO) { getArtistDetail.invoke(retrofit, artistName) }
                imageView.loadImage(artist?.mediumImageUrl ?: RANDOM_IMAGE)
            }
        }
    }

    fun onArtistClicked(artistName: String) {
        _detail.value = Event(artistName)
    }

    fun loadRecommendations(retrofit: Retrofit) {
        ConnectivityController.runIfConnected {
            launch {
                _loading.value = true
                val country = _country.value?.peekContent() ?: DEFAULT_COUNTRY
                val artists = withContext(Dispatchers.IO) { getRecommendedArtists.invoke(retrofit, country) }
                _artists.value = artists
                _loading.value = false
            }
        }
    }

    private fun retrieveCountry(retrofit: Retrofit, latitude: String, longitude: String, mapsKey: String) {
        ConnectivityController.runIfConnected {
            launch {
                val userCountry = withContext(Dispatchers.IO) { getCountry.invoke(retrofit, "$latitude,$longitude", mapsKey) }
                val retrievedCountry = when (userCountry.isEmpty()) {
                    true -> DEFAULT_COUNTRY
                    else -> userCountry
                }
                _country.value = Event(retrievedCountry)
            }
        }
    }

    private companion object {
        private const val RANDOM_IMAGE = "https://picsum.photos/200/300"
        private const val DEFAULT_COUNTRY = "usa"
    }
}