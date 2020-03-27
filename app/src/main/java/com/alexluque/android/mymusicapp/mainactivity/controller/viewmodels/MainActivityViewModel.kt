package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import ArtistData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope

class MainActivityViewModel : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    sealed class UiModel {
        object Loading : UiModel()
        object Search : UiModel()
        class Content(val artists: List<ArtistData>) : UiModel()
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

    init {
        initScope()
    }

    override fun onCleared() {
        cancelScope()
    }

    private fun refresh() {
        innerModel.value = UiModel.Loading
        innerModel.value = UiModel.Content(loadData())
    }

    private fun loadData(): List<ArtistData> =
        // TODO: retrieves all artists with existing favourite songs in the DB
        listOf(
            ArtistData(
                119,
                "Metallica",
                "https://www.deezer.com/artist/119",
                "https://api.deezer.com/artist/119/image",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/56x56-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/250x250-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/500x500-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/1000x1000-000000-80-0-0.jpg",
                31,
                5573986,
                true,
                "https://api.deezer.com/artist/119/top?limit=50",
                "artist"
            ),
            ArtistData(
                1245,
                "Trivium",
                "https://www.deezer.com/artist/1245",
                "https://api.deezer.com/artist/1245/image",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/56x56-000000-80-0-0.jpg",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/250x250-000000-80-0-0.jpg",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/500x500-000000-80-0-0.jpg",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/1000x1000-000000-80-0-0.jpg",
                35,
                236736,
                true,
                "https://api.deezer.com/artist/1245/top?limit=50",
                "artist"
            )
        )

    fun onArtistClicked(artistName: String) {
        innerNavigation.value = Event(UiModel.Navigation(artistName))
    }

    fun onRecommendClicked(country: String) {
        innerRecommendation.value = Event(UiModel.Recommendations(country))
    }

    fun onSearchClicked() {
        innerSearch.value = Event(UiModel.Search)
    }
}