package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.ui.main.MainViewModel.Companion.DEFAULT_COUNTRY
import com.example.android.domain.RecommendedArtist
import com.example.android.usecases.GetArtistDetail
import com.example.android.usecases.GetRecommendedArtists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModel(
    country: String,
    activityTitle: String,
    private val getArtistDetail: GetArtistDetail,
    private val getRecommendedArtists: GetRecommendedArtists
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _artists = MutableLiveData<List<RecommendedArtist>>()
    val artists: LiveData<List<RecommendedArtist>> get() = _artists

    private val _detail = MutableLiveData<Event<String>>()
    val detail: LiveData<Event<String>> get() = _detail

    val title = activityTitle

    init {
        initScope()
        loadRecommendations(country)
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun loadImage(artistName: String, imageView: ImageView) {
        ConnectivityController.runIfConnected {
            launch {
                val artist = withContext(Dispatchers.IO) { getArtistDetail.invoke(artistName) }
                imageView.loadImage(artist?.mediumImageUrl ?: RANDOM_IMAGE)
            }
        }
    }

    fun onArtistClicked(artistName: String) {
        _detail.value = Event(artistName)
    }

    private fun loadRecommendations(country: String = DEFAULT_COUNTRY) {
        ConnectivityController.runIfConnected {
            launch {
                _loading.value = true
                val artists = withContext(Dispatchers.IO) { getRecommendedArtists.invoke(country) }
                _artists.value = artists
                _loading.value = false
            }
        }
    }

    private companion object {
        private const val RANDOM_IMAGE = "https://picsum.photos/200/300"
    }
}

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModelFactory(
    private val country: String,
    private val activityTitle: String,
    private val getArtistDetail: GetArtistDetail,
    private val getRecommendedArtists: GetRecommendedArtists
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RecommendationsViewModel(
            country,
            activityTitle,
            getArtistDetail,
            getRecommendedArtists
        ) as T
}