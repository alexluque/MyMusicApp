package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.MusicoveryArtist
import com.alexluque.android.mymusicapp.mainactivity.model.network.repositories.getArtist
import com.alexluque.android.mymusicapp.mainactivity.model.network.repositories.getArtistsByLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModel(country: String) :
    ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _artists = MutableLiveData<List<MusicoveryArtist>>()
    val artists: LiveData<List<MusicoveryArtist>> get() = _artists

    private val _detail = MutableLiveData<Event<String>>()
    val detail: LiveData<Event<String>> get() = _detail

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
                val artist = withContext(Dispatchers.IO) { getArtist(artistName) }
                imageView.loadImage(artist?.picture_medium ?: RANDOM_IMAGE)
            }
        }
    }

    fun onArtistClicked(artistName: String) {
        _detail.value = Event(artistName)
    }

    private fun loadRecommendations(country: String) {
        ConnectivityController.runIfConnected {
            launch {
                _loading.value = true
                val artists = withContext(Dispatchers.IO) { getArtistsByLocation(country) }
                _artists.value = artists.artists.artist
                _loading.value = false
            }
        }
    }

    companion object {
        private const val RANDOM_IMAGE = "https://picsum.photos/200/300"
    }
}

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModelFactory(private val country: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RecommendationsViewModel(country) as T
}