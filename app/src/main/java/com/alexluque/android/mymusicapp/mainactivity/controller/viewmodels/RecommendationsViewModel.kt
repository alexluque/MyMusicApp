package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import MusicoveryArtist
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.model.repositories.getArtistsByLocation
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.RecommendedArtistsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModel(
    val country: String,
    val adapter: RecommendedArtistsAdapter,
    val data: MutableList<MusicoveryArtist>
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    sealed class UiModel {
        object Loading : UiModel()
        class Content(val block: Unit) : UiModel()
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

    private fun refresh() {
        innerModel.value = UiModel.Loading
        innerModel.value = UiModel.Content(showRecommendations(country, adapter, data))
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }

    fun onArtistClicked(artistName: String) {
        innerModel.value = UiModel.Navigation(artistName)
    }

    private fun showRecommendations(country: String = DEFAULT_COUNTRY, adapter: RecommendedArtistsAdapter, data: MutableList<MusicoveryArtist>) {
        ConnectivityController.runIfConnected {
            launch {
                val artists = withContext(Dispatchers.IO) {
                    getArtistsByLocation(country)
                }
                adapter.updateData(data as MutableList<Any>, artists.artists.artist)
//                contract?.hideProgress()
//                contract?.makeSnackbar(context?.getString(R.string.country_recommendations) + " ${country.toUpperCase()}")
            }
        }
    }

    companion object {
        const val DEFAULT_COUNTRY = "usa"
    }
}

@Suppress("UNCHECKED_CAST")
class RecommendationsViewModelFactory(val country: String, val adapter: RecommendedArtistsAdapter, val data: MutableList<MusicoveryArtist>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RecommendationsViewModel(country, adapter, data) as T
}