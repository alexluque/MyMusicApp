package com.alexluque.android.mymusicapp.mainactivity.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexluque.android.mymusicapp.mainactivity.ui.common.Event
import com.alexluque.android.mymusicapp.mainactivity.ui.common.MyCoroutineScope
import com.example.android.domain.FavouriteArtist
import com.example.android.usecases.GetFavouriteArtistSongs
import com.example.android.usecases.GetFavouriteArtists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val getFavouriteArtists: GetFavouriteArtists,
    private val getFavouriteArtistSongs: GetFavouriteArtistSongs
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

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

    override fun onCleared() = cancelScope()

    fun loadArtists() = launch {
        _loading.value = true
        val artists = withContext(Dispatchers.IO) { getFavouriteArtists.invoke() }
        artists.forEach { it.favouriteSongs = getFavouriteArtistSongs.invoke(it.id) }
        _artists.value = artists
        _loading.value = false
    }

    fun onArtistClicked(artistName: String) {
        _artistName.value = Event(artistName)
    }
}