package com.alexluque.android.mymusicapp.mainactivity.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alexluque.android.mymusicapp.mainactivity.ui.main.MainViewModel
import com.example.android.domain.FavouriteArtist
import com.example.android.usecases.GetFavouriteArtistSongs
import com.example.android.usecases.GetFavouriteArtists
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getFavouriteArtistsStub: GetFavouriteArtists

    @Mock
    private lateinit var getFavouriteArtistSongsStub: GetFavouriteArtistSongs

    @Mock
    private lateinit var loadingObserverMock: Observer<Boolean>

    @Mock
    private lateinit var artistsObserverMock: Observer<List<FavouriteArtist>>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(getFavouriteArtistsStub, getFavouriteArtistSongsStub, Dispatchers.Unconfined)
    }

    @Test
    fun `Loading is false initially, turns true when loadArtists and becomes false at the end`() {
        viewModel.loading.observeForever(loadingObserverMock)

        verify(loadingObserverMock).onChanged(false)

        viewModel.loadArtists()

        verify(loadingObserverMock).onChanged(true)
    }

    @Test
    fun `Artists and their number of existing songs are loaded if there's data on the database`() {
        runBlockingTest {
            val artistId: Long = 1
            val artists = listOf(FavouriteArtist(artistId, "artist", "url"))

            whenever(getFavouriteArtistsStub.invoke()).thenReturn(artists)
            whenever(getFavouriteArtistSongsStub.invoke(artistId)).thenReturn(1)

            viewModel.artists.observeForever(artistsObserverMock)

            viewModel.loadArtists()

            verify(artistsObserverMock).onChanged(artists)
        }
    }
}