package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import MusicoveryArtist
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getArtist
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getArtistsByLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModel(private val country: String) :
    ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    sealed class UiModel {
        object Loading : UiModel()
        class Content(val artists: List<MusicoveryArtist>) : UiModel()
        class Navigation(val artistName: String) : UiModel()
    }

    private val innerModel = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (innerModel.value == null) refresh()
            return innerModel
        }

    init {
        initScope()
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    private fun refresh() {
        innerModel.value = UiModel.Loading
        showRecommendations(country)
    }

    fun onArtistClicked(artistName: String) {
        innerModel.value = UiModel.Navigation(artistName)
    }

    fun loadImage(artistName: String, imageView: ImageView) {
        ConnectivityController.runIfConnected {
            launch {
                val artist = withContext(Dispatchers.IO) {
                    getArtist(artistName)
                }
                imageView.loadImage(artist?.picture_medium ?: RANDOM_IMAGE)
            }
        }
    }

    private fun showRecommendations(country: String = DEFAULT_COUNTRY) {
        ConnectivityController.runIfConnected {
            launch {
                val artists = withContext(Dispatchers.IO) {
                    getArtistsByLocation(country)
                }
                innerModel.value = UiModel.Content(artists.artists.artist)
            }
        }
    }

    companion object {
        const val DEFAULT_COUNTRY = "usa"
        private const val RANDOM_IMAGE = "https://picsum.photos/200/300"
    }
}

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModelFactory(val country: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RecommendationsViewModel(country) as T
}