package com.alexluque.android.mymusicapp.mainactivity.viewmodel

import android.net.NetworkRequest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailViewModel
import com.alexluque.android.domain.Artist
import com.alexluque.android.domain.ArtistDetail
import com.alexluque.android.domain.Song
import com.alexluque.android.usecases.HandleFavourite
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.lenient
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ArtistDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var handleFavouriteStub: HandleFavourite

    @Mock
    private lateinit var loadingObserverMock: Observer<Boolean>

    @Mock
    private lateinit var artistDetailNameObserverMock: Observer<String?>

    @Mock
    private lateinit var currentArtistObserverMock: Observer<ArtistDetail?>

    @Mock
    private lateinit var imageUrlObserverMock: Observer<String>

    @Mock
    private lateinit var songsObserverMock: Observer<List<Song>>

    @Mock
    private lateinit var retrofitMock: RetrofitBuilder

    @Mock
    private lateinit var deezerInstanceMock: Retrofit

    @Mock
    private lateinit var musicoveryInstanceMock: Retrofit

    @Spy
    private lateinit var connectivityMock: ConnectivityController

    @Mock
    private lateinit var networkMock: NetworkRequest

    private val artistName: String? = "artist"

    private lateinit var viewModel: ArtistDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ArtistDetailViewModel(handleFavouriteStub, Dispatchers.Unconfined, connectivityMock)

        // This mock should happen to bypass mandatory existing internet connection while running some methods
        // despite Mockito throws UnnecessaryStubbingException.
        // lenient() avoids Mockito's strict stubbing.
        // Notice that the 'whenever' wrapper could't be used after lenient().
        // `when` should be used instead.
        lenient().`when`(connectivityMock.networkRequest).thenReturn(networkMock)
        whenever(retrofitMock.deezerInstance).thenReturn(deezerInstanceMock)
        whenever(retrofitMock.musicoveryInstance).thenReturn(musicoveryInstanceMock)
        connectivityMock.hasInternet = true
    }

    @Test
    fun `Loading is false initially, turns true when loadData and becomes false at the end`() {
        viewModel.loading.observeForever(loadingObserverMock)

        viewModel.loadData(retrofitMock, artistName)

        verify(loadingObserverMock).onChanged(true)

        verify(loadingObserverMock).onChanged(false)
    }

    @Test
    fun `artistDetailName set to null when no artist retrieved from Deezer`() {
        runBlockingTest {
            whenever(handleFavouriteStub.getArtistDetail(deezerInstanceMock, artistName!!)).thenReturn(null)

            viewModel.artistDetailName.observeForever(artistDetailNameObserverMock)

            viewModel.loadData(retrofitMock, artistName)

            verify(artistDetailNameObserverMock).onChanged(null)
        }
    }

    @Test
    fun `Full info is retrieved when Deezer artist is found`() {
        runBlockingTest {
            val artist = ArtistDetail(1, artistName!!, "big", "medium")
            val musicoveryArtist = Mockito.mock(Artist::class.java)
            val songs = emptyList<Song>()

            whenever(handleFavouriteStub.getArtistDetail(deezerInstanceMock, artistName)).thenReturn(artist)
            whenever(handleFavouriteStub.getArtist(retrofitMock.musicoveryInstance, artist.name)).thenReturn(musicoveryArtist)
            whenever(handleFavouriteStub.getArtistSongs(retrofitMock.deezerInstance, artistName)).thenReturn(songs)
            whenever(musicoveryArtist.mbid).thenReturn("mbid")

            viewModel.currentArtist.observeForever(currentArtistObserverMock)
            viewModel.imageUrl.observeForever(imageUrlObserverMock)
            viewModel.songs.observeForever(songsObserverMock)
            viewModel.artistDetailName.observeForever(artistDetailNameObserverMock)

            viewModel.loadData(retrofitMock, artistName)

            verify(currentArtistObserverMock).onChanged(artist)
            verify(imageUrlObserverMock).onChanged(artist.bigImageUrl)
            verify(songsObserverMock).onChanged(songs)
            verify(artistDetailNameObserverMock).onChanged(artist.name)
            verify(handleFavouriteStub, times(1)).getArtistInfo(retrofitMock.musicoveryInstance, musicoveryArtist.mbid)
        }
    }

    @Test
    fun `Data not loaded when no internet connection`() {
        connectivityMock.hasInternet = false

        viewModel.loading.observeForever(loadingObserverMock)

        viewModel.loadData(retrofitMock, artistName)

        verify(loadingObserverMock, never()).onChanged(true)
    }

    @Test
    fun `Database favourite songs retrieved when loadFavouriteSongs called`() {
        runBlockingTest {
            whenever(handleFavouriteStub.getFavouriteSongs()).thenReturn(emptyList<Song>())

            viewModel.loadFavouriteSongs()

            verify(handleFavouriteStub, times(1)).getFavouriteSongs()
        }
    }
}