package com.alexluque.android.mymusicapp.mainactivity.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.example.android.domain.FavouriteArtist
import com.example.android.usecases.GetCountry
import com.example.android.usecases.GetFavouriteArtistSongs
import com.example.android.usecases.GetFavouriteArtists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val getFavouriteArtists: GetFavouriteArtists,
    private val getFavouriteArtistSongs: GetFavouriteArtistSongs,
    private val getCountry: GetCountry
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    private val _artists = MutableLiveData<List<FavouriteArtist>>()
    val artists: LiveData<List<FavouriteArtist>> get() = _artists

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _artistName = MutableLiveData<Event<String>>()
    val artistName: LiveData<Event<String>> get() = _artistName

    private val _country = MutableLiveData<Event<String>>()
    val country: LiveData<Event<String>> get() = _country

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

    fun onRecommendClicked(mapsKey: String, latitude: Double, longitude: Double) =
        ConnectivityController.runIfConnected {
            launch {
                val userCountry = withContext(Dispatchers.IO) { getCountry.invoke("$latitude,$longitude", mapsKey) }
                val country = when (userCountry.isEmpty()) {
                    true -> DEFAULT_COUNTRY
                    else -> userCountry
                }
                _country.value = Event(country)
            }
        }

    companion object {
        const val DEFAULT_COUNTRY = "usa"
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val getFavouriteArtists: GetFavouriteArtists,
    private val getFavouriteArtistSongs: GetFavouriteArtistSongs,
    private val getCountry: GetCountry
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainViewModel(getFavouriteArtists, getFavouriteArtistSongs, getCountry) as T
}