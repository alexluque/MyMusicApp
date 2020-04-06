package com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
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
    artistName: String?,
    application: Application
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    class Favourite(val star: ImageView, val songName: String, val newFavourite: Boolean)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private val _songs = MutableLiveData<List<SongData>>()
    val songs: LiveData<List<SongData>> = _songs

    private val _favourite = MutableLiveData<Event<Favourite>>()
    val favourite: LiveData<Event<Favourite>> = _favourite

    private val dbRepository: DatabaseRepository
    private val favouriteSongs = mutableListOf<Song>()

    private val _currentArtist = MutableLiveData<ArtistData?>()
    val currentArtist: LiveData<ArtistData?> = _currentArtist

    init {
        initScope()
        val db = FavouritesRoomDatabase.getDatabase(application)
        dbRepository = DatabaseRepository(db.artistDao(), db.songDao())
        loadData(artistName)
    }

    override fun onCleared() = cancelScope()

    fun loadData(artistName: String?) {
        ConnectivityController.runIfConnected {
            val name = artistName ?: String()

            launch {
                _loading.value = true
                val artist = withContext(Dispatchers.IO) { getArtist(name) }
                _currentArtist.value = artist
                _imageUrl.value = artist?.picture_big
                _songs.value = artist?.let { withContext(Dispatchers.IO) { getSongs(name).data } }
                _loading.value = false
            }
        }

        loadFavouriteSongs()
    }

    private fun loadFavouriteSongs() = launch {
        favouriteSongs.clear()
        favouriteSongs.addAll(withContext(Dispatchers.IO) { dbRepository.getSongs() })
    }

    fun onFavouriteClicked(star: ImageView, songId: Long, title: String, album: String?) {
        _currentArtist.value?.let {
            launch {
                val song = Song(songId, title, album, it.id)
                val artist = Artist(it.id, it.name, it.picture_medium)

                if (isFavourite(songId).not()) {
                    val artistExists = withContext(Dispatchers.IO) { dbRepository.artistExists(it.id) }

                    if (artistExists.not())
                        dbRepository.insertArtist(artist)

                    try {
                        dbRepository.insertSong(song)
                    } catch (e: SQLiteConstraintException) {
                    }

                    _favourite.value = Event(Favourite(star, title, true))
                    loadFavouriteSongs()
                } else {
                    dbRepository.deleteSong(song)

                    if (dbRepository.artistHasSongs(it.id).not())
                        dbRepository.deleteArtist(artist)

                    _favourite.value = Event(Favourite(star, title, false))
                    loadFavouriteSongs()
                }
            }
        }
    }

    fun isFavourite(songId: Long): Boolean = favouriteSongs.any { it.id == songId }

    companion object {
        const val ARTIST_NAME = "artist name"
    }
}

@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModelFactory(
    private val artistName: String?,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ArtistDetailViewModel(artistName, application) as T
}