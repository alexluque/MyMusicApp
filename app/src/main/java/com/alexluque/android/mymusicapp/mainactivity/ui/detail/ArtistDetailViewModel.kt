package com.alexluque.android.mymusicapp.mainactivity.ui.detail

import android.database.sqlite.SQLiteConstraintException
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.common.Event
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ScopedViewModel
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.updateData
import com.example.android.domain.Artist
import com.example.android.domain.ArtistDetail
import com.example.android.domain.FavouriteArtist
import com.example.android.domain.Song
import com.example.android.usecases.HandleFavourite
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
class ArtistDetailViewModel(
    private val handleFavourite: HandleFavourite,
    uiDispatcher: CoroutineDispatcher,
    private val connectivity: ConnectivityController
) : ScopedViewModel(uiDispatcher) {

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

    private val _artistDetailName = MutableLiveData<String?>()
    val artistDetailName: LiveData<String?> get() = _artistDetailName

    private val favouriteSongs = mutableListOf<Song>()

    private var musicoveryArtist: Artist? = null

    init {
        initScope()
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

    fun loadData(retrofit: RetrofitBuilder, artistName: String? = String()) {
        connectivity.runIfConnected {
            launch {
                _loading.value = true

                val artist = handleFavourite.getArtistDetail(retrofit.deezerInstance, artistName!!)
                artist?.let {
                    _currentArtist.value = it
                    musicoveryArtist = handleFavourite.getArtist(retrofit.musicoveryInstance, it.name)
                    _imageUrl.value = it.bigImageUrl
                    _songs.value = handleFavourite.getArtistSongs(retrofit.deezerInstance, artistName)
                    _artistDetailName.value = it.name
                } ?: run { _artistDetailName.value = null }

                // Ideally, this should be placed next to variable's assignment
                // but Musicovery's API needs at least 1sec between calls
                musicoveryArtist?.let { ma ->
                    if (ma.mbid.isNotEmpty()) {
                        val info: Artist? = handleFavourite.getArtistInfo(retrofit.musicoveryInstance, ma.mbid)
                        info.let {
                            ma.genres = it?.genres.toString()
                            ma.region = it?.region?.toString()?.replace(EMPTY_OBJECT, String())?.capitalize(Locale.ROOT)
                            ma.country = it?.country?.toString()?.replace(EMPTY_OBJECT, String())
                        }
                    }
                }

                _loading.value = false
            }
        }
    }

    fun loadFavouriteSongs() {
        launch {
            favouriteSongs.clear()
            favouriteSongs.addAll(handleFavourite.getFavouriteSongs())
        }
    }

    fun onFavouriteClicked(star: ImageView, songId: Long, title: String, album: String?) {
        _currentArtist.value?.let {
            launch {
                val song = Song(songId, title, album, it.id)
                val favouriteArtist = FavouriteArtist(it.id, it.name, it.bigImageUrl)
                favouriteArtist.genre = musicoveryArtist?.genres.toString()
                favouriteArtist.regionAndCountry =
                    setRegionCountry(musicoveryArtist?.region.toString(), musicoveryArtist?.country.toString())

                val isFavouriteSong = isFavouriteSong(songId)
                manageFavourite(song, it, favouriteArtist, isFavouriteSong)
                _favourite.value = Event(Favourite(star, title, !isFavouriteSong))
                loadFavouriteSongs()
            }
        }
    }

    fun isFavouriteSong(songId: Long): Boolean = favouriteSongs.any { it.id == songId }

    private suspend fun manageFavourite(
        song: Song,
        it: ArtistDetail,
        favouriteArtist: FavouriteArtist,
        isFavouriteSong: Boolean
    ) {
        if (isFavouriteSong) {
            handleFavourite.deleteSong(song)

            if (!handleFavourite.artistHasSongs(it.id))
                handleFavourite.deleteArtist(favouriteArtist)
        } else {
            if (!handleFavourite.isFavouriteArtist(it.id))
                handleFavourite.insertArtist(favouriteArtist)

            try {
                handleFavourite.insertSong(song)
            } catch (e: SQLiteConstraintException) {
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

    fun updateDetail(
        viewAdapter: ArtistDetailAdapter,
        songs: List<Song>,
        appBarLayout: AppBarLayout?,
        recyclerView: RecyclerView
    ) {
        viewAdapter.updateData(viewAdapter.songs as MutableList<Any>, songs)
        appBarLayout?.setExpanded(true)
        recyclerView.scrollToPosition(INITIAL_POSITION)
    }

    companion object {
        const val ARTIST_NAME = "artist name"
        private const val EMPTY_OBJECT = "{}"
        private const val INITIAL_POSITION = 0
    }
}