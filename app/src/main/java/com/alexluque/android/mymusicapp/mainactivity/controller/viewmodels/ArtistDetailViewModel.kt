package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Song
import com.alexluque.android.mymusicapp.mainactivity.model.database.repositories.DatabaseRepository
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.ArtistData
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongData
import com.alexluque.android.mymusicapp.mainactivity.model.network.repositories.getArtist
import com.alexluque.android.mymusicapp.mainactivity.model.network.repositories.getSongs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModel(
    private val artistName: String?,
    application: Application
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    sealed class UiModel {
        object Loading : UiModel()
        object Search : UiModel()
        class Content(val artist: ArtistData?, val songs: List<SongData>) : UiModel()
        class Favourite(val star: ImageView, val songName: String, val newFavourite: Boolean) : UiModel()
    }

    private val innerModel = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (innerModel.value == null) refresh()
            return innerModel
        }
    private val innerSearch = MutableLiveData<Event<UiModel>>()
    val search: LiveData<Event<UiModel>> = innerSearch

    private val dbRepository: DatabaseRepository
    private var currentArtist: ArtistData? = null
    private val favouriteSongs = mutableListOf<Song>()

    init {
        initScope()
        val db = FavouritesRoomDatabase.getDatabase(application)
        dbRepository = DatabaseRepository(db.artistDao(), db.songDao())
        loadFavouriteSongs()
    }

    override fun onCleared() {
        cancelScope()
    }

    private fun refresh() {
        innerModel.value = UiModel.Loading
        loadData(artistName)
    }

    private fun loadFavouriteSongs() = launch {
        favouriteSongs.clear()
        favouriteSongs.addAll(withContext(Dispatchers.IO) { dbRepository.getSongs() })
    }

    fun loadData(artistName: String?) {
        ConnectivityController.runIfConnected {
            val name = artistName ?: String()

            launch {
                val artist = withContext(Dispatchers.IO) { getArtist(name) }

                when (artist == null) {
                    true -> {
                        currentArtist = null
                        innerModel.value = UiModel.Content(artist, listOf<SongData>())
                    }
                    else -> {
                        currentArtist = artist
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

    fun isFavourite(songId: Long): Boolean = favouriteSongs.any { it.id == songId }

    fun onFavouriteClicked(star: ImageView, songId: Long, title: String, album: String?) {
        currentArtist?.let {
            launch {
                val song = Song(songId, title, album, it.id)
                val artist = Artist(it.id, it.name, it.picture_medium)

                if (isFavourite(songId).not()) {
                    val artistExists = withContext(Dispatchers.IO) { dbRepository.artistExists(it.id) }

                    if (artistExists.not())
                        dbRepository.insertArtist(artist)

                    dbRepository.insertSong(song)

                    innerModel.value = UiModel.Favourite(star, title, true)
                } else {
                    dbRepository.deleteSong(song)

                    if (dbRepository.artistHasSongs(it.id).not())
                        dbRepository.deleteArtist(artist)

                    innerModel.value = UiModel.Favourite(star, title, false)
                }

                loadFavouriteSongs()
            }
        }
    }

    companion object {
        const val ARTIST_NAME = "artist name"
    }
}

@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModelFactory(
    private val artistName: String?,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ArtistDetailViewModel(artistName, application) as T
}