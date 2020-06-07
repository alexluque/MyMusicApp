package com.alexluque.android.mymusicapp.mainactivity.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexluque.android.mymusicapp.mainactivity.ui.common.Event
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ScopedViewModel
import com.alexluque.android.domain.FavouriteArtist
import com.alexluque.android.usecases.GetFavouriteArtistSongs
import com.alexluque.android.usecases.GetFavouriteArtists
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(
    private val getFavouriteArtists: GetFavouriteArtists,
    private val getFavouriteArtistSongs: GetFavouriteArtistSongs,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    private val _artists = MutableLiveData<List<FavouriteArtist>>()
    val artists: LiveData<List<FavouriteArtist>> get() = _artists

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _artistName = MutableLiveData<Event<String>>()
    val artistName: LiveData<Event<String>> get() = _artistName

    init {
        initScope()
        loadArtists()
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

    fun loadArtists() = launch {
        _loading.value = true

        val artists: List<FavouriteArtist>? = getFavouriteArtists.invoke()
        artists?.let {
            it.forEach { artist ->
                artist.favouriteSongs = getFavouriteArtistSongs.invoke(artist.id)
            }
        }
        _artists.value = artists

        _loading.value = false
    }

    fun onArtistClicked(artistName: String) {
        _artistName.value = Event(artistName)
    }
}