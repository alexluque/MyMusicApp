package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.database.repositories.DatabaseRepository
import com.alexluque.android.mymusicapp.mainactivity.model.network.repositories.getCountry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    application: Application
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> = _artists

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _artistName = MutableLiveData<Event<String>>()
    val artistName: LiveData<Event<String>> = _artistName

    private val _country = MutableLiveData<Event<String>>()
    val country: LiveData<Event<String>> = _country

    private val dbRepository: DatabaseRepository

    init {
        initScope()
        val db = FavouritesRoomDatabase.getDatabase(application)
        dbRepository = DatabaseRepository(db.artistDao(), db.songDao())
        loadArtists()
    }

    override fun onCleared() = cancelScope()

    fun loadArtists() = launch {
        _loading.value = true
        val artists = withContext(Dispatchers.IO) { dbRepository.getFavouriteArtists() }
        artists.forEach { it.favouriteSongs = dbRepository.getArtistSongs(it.id) }
        _artists.value = artists
        _loading.value = false
    }

    fun onArtistClicked(artistName: String) {
        _artistName.value = Event(artistName)
    }

    fun onRecommendClicked(mapsKey: String, latitude: Double, longitude: Double) =
        ConnectivityController.runIfConnected {
            launch {
                val userCountry = withContext(Dispatchers.IO) { getCountry("$latitude,$longitude", mapsKey) }
                val country = when (userCountry.isNullOrEmpty()) {
                    true -> DEFAULT_COUNTRY
                    else -> userCountry
                }
                _country.value = Event(country)
            }
        }

    private companion object {
        private const val DEFAULT_COUNTRY = "usa"
    }
}

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainActivityViewModel(application) as T
}