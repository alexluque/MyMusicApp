package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import ArtistData
import SongData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getArtist
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getSongs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModel(private val artistName: String?) :
    ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    sealed class UiModel {
        object Loading : UiModel()
        object Search : UiModel()
        class Content(val artist: ArtistData?, val songs: List<SongData>) : UiModel()
    }

    private val innerModel = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (innerModel.value == null) refresh()
            return innerModel
        }
    private val innerSearch = MutableLiveData<Event<UiModel>>()
    val search: LiveData<Event<UiModel>> = innerSearch

    init {
        initScope()
    }

    override fun onCleared() {
        cancelScope()
    }

    private fun refresh() {
        innerModel.value = UiModel.Loading
        loadData(artistName)
    }

    fun loadData(artistName: String?) {
        ConnectivityController.runIfConnected {
            val name = artistName ?: String()

            launch {
                val artist = withContext(Dispatchers.IO) { getArtist(name) }

                when (artist == null) {
                    true -> innerModel.value = UiModel.Content(artist, listOf<SongData>())
                    else -> {
                        val songs = withContext(Dispatchers.IO) { getSongs(name) }
                        innerModel.value = UiModel.Content(artist, songs.data)
                    }
                }
            }
        }
    }

    fun onSearchClicked() {
        innerSearch.value = Event(UiModel.Search)
    }

    companion object {
        const val ARTIST_NAME = "artist name"
    }
}

@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModelFactory(private val artistName: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ArtistDetailViewModel(artistName) as T
}