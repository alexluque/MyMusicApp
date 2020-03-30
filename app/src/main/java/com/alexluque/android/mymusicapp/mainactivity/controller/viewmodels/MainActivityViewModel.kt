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

    sealed class UiModel {
        object Loading : UiModel()
        object Search : UiModel()
        class Content(val artists: List<Artist>) : UiModel()
        class Navigation(val artistName: String) : UiModel()
        class Recommendations(val country: String) : UiModel()
    }

    private val innerModel = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (innerModel.value == null) refresh()
            return innerModel
        }
    private val innerNavigation = MutableLiveData<Event<UiModel>>()
    val navigation: LiveData<Event<UiModel>> = innerNavigation
    private val innerRecommendation = MutableLiveData<Event<UiModel>>()
    val recommendation: LiveData<Event<UiModel>> = innerRecommendation
    private val innerSearch = MutableLiveData<Event<UiModel>>()
    val search: LiveData<Event<UiModel>> = innerSearch

    private val dbRepository: DatabaseRepository

    init {
        initScope()
        val db = FavouritesRoomDatabase.getDatabase(application)
        dbRepository = DatabaseRepository(db.artistDao(), db.songDao())
    }

    override fun onCleared() {
        cancelScope()
    }

    private fun refresh() {
        innerModel.value = UiModel.Loading
        loadArtists()
    }

    private fun loadArtists() = launch {
        val artists = withContext(Dispatchers.IO) { dbRepository.getFavouriteArtists() }
        artists.forEach { it.favouriteSongs = dbRepository.getArtistSongs(it.id) }
        innerModel.value = UiModel.Content(artists)
    }

    fun onArtistClicked(artistName: String) {
        innerNavigation.value = Event(UiModel.Navigation(artistName))
    }

    fun onRecommendClicked(mapsKey: String, latitude: Double, longitude: Double) =
        ConnectivityController.runIfConnected {
            launch {
                val userCountry = withContext(Dispatchers.IO) { getCountry("$latitude,$longitude", mapsKey) }
                val country = when (userCountry.isNullOrEmpty()) {
                    true -> DEFAULT_COUNTRY
                    else -> userCountry
                }
                innerRecommendation.value = Event(UiModel.Recommendations(country))
            }
        }

    fun onSearchClicked() {
        innerSearch.value = Event(UiModel.Search)
    }

    private companion object {
        private const val DEFAULT_COUNTRY = "usa"
    }
}

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainActivityViewModel(application) as T
}