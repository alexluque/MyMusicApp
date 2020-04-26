package com.alexluque.android.mymusicapp.mainactivity.ui.detail

import android.database.sqlite.SQLiteConstraintException
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.Event
import com.alexluque.android.mymusicapp.mainactivity.controller.MyCoroutineScope
import com.alexluque.android.mymusicapp.mainactivity.model.emptyDomainArtistInfo
import com.example.android.domain.*
import com.example.android.usecases.HandleFavourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import java.util.*

@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModel(
    artistName: String?,
    private val handleFavourite: HandleFavourite
) : ViewModel(), MyCoroutineScope by MyCoroutineScope.Implementation() {

    class Favourite(val star: ImageView, val songName: String, val newFavourite: Boolean)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    private val _favourite = MutableLiveData<Event<Favourite>>()
    val favourite: LiveData<Event<Favourite>> get() = _favourite

    private val _currentArtist = MutableLiveData<ArtistDetail?>()
    val currentArtist: LiveData<ArtistDetail?> get() = _currentArtist

    private val favouriteSongs = mutableListOf<Song>()

    private var musicoveryArtist: Artist? = null
    private var artistInfo: ArtistInfo? = null

    init {
        initScope()
        loadData(artistName)
    }

    override fun onCleared() = cancelScope()

    fun loadData(artistName: String?) {
        ConnectivityController.runIfConnected {
            val name = artistName ?: String()

            launch {
                _loading.value = true
                val artist = withContext(Dispatchers.IO) { handleFavourite.getArtistDetail(name) }
                _currentArtist.value = artist
                artist?.let { musicoveryArtist = withContext(Dispatchers.IO) { handleFavourite.getArtist(it.name) } }
                _imageUrl.value = artist?.bigImageUrl
                _songs.value = artist?.let { withContext(Dispatchers.IO) { handleFavourite.getArtistSongs(name) } }

                musicoveryArtist?.let {
                    if (artistInfo == null)
                        artistInfo = try {
                            withContext(Dispatchers.IO) { handleFavourite.getArtistInfo(it.mbid) }
                        } catch (ex: IllegalArgumentException) {
                            emptyDomainArtistInfo()
                        }
                }

                _loading.value = false
            }
        }

        loadFavouriteSongs()
    }

    private fun loadFavouriteSongs() = launch {
        favouriteSongs.clear()
        favouriteSongs.addAll(withContext(Dispatchers.IO) { handleFavourite.getFavouriteSongs() })
    }

    @ExperimentalStdlibApi
    fun onFavouriteClicked(star: ImageView, songId: Long, title: String, album: String?) {
        _currentArtist.value?.let {
            launch {
                val song = Song(songId, title, album, it.id)
                val favouriteArtist = FavouriteArtist(it.id, it.name, it.bigImageUrl)

                musicoveryArtist?.let {
                    artistInfo?.let {
                        favouriteArtist.genre = it.genres.toString()
                        val region = it.region?.toString()?.replace(EMPTY_OBJECT, String())?.capitalize(Locale.ROOT)
                        val country = it.country?.toString()?.replace(EMPTY_OBJECT, String())
                        favouriteArtist.regionAndCountry = setRegionCountry(region, country)
                    }
                }

                if (isFavourite(songId).not()) {
                    val artistExists = withContext(Dispatchers.IO) { handleFavourite.isFavouriteArtist(it.id) }

                    if (artistExists.not())
                        handleFavourite.insertArtist(favouriteArtist)

                    try {
                        handleFavourite.insertSong(song)
                    } catch (e: SQLiteConstraintException) {
                    }

                    _favourite.value = Event(Favourite(star, title, true))
                    loadFavouriteSongs()
                } else {
                    handleFavourite.deleteSong(song)

                    if (handleFavourite.artistHasSongs(it.id).not())
                        handleFavourite.deleteArtist(favouriteArtist)

                    _favourite.value = Event(Favourite(star, title, false))
                    loadFavouriteSongs()
                }
            }
        }
    }

    private fun setRegionCountry(region: String?, country: String?): String =
        if (region.isNullOrEmpty().not() && country.isNullOrEmpty().not())
            "$region, $country"
        else if (region.isNullOrEmpty() && country.isNullOrEmpty().not())
            country!!
        else if (region.isNullOrEmpty().not() && country.isNullOrEmpty())
            region!!
        else
            String()

    fun isFavourite(songId: Long): Boolean = favouriteSongs.any { it.id == songId }

    companion object {
        const val ARTIST_NAME = "artist name"
        private const val EMPTY_OBJECT = "{}"
    }
}

@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModelFactory(
    private val artistName: String?,
    private val handleFavourite: HandleFavourite
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ArtistDetailViewModel(artistName, handleFavourite) as T
}