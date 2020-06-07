package com.alexluque.android.mymusicapp.mainactivity.unit

import com.alexluque.android.mymusicapp.mainactivity.model.database.RoomDataSource
import com.alexluque.android.domain.FavouriteArtist
import com.alexluque.android.domain.Song
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
class RoomDataSourceTest {

    @Mock
    private lateinit var dsMock: RoomDataSource

    private val fakeArtist = FavouriteArtist(1, "name", "url")
    private val fakeSong = Song(1, "title", "album", fakeArtist.id)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getFavouriteArtists() {
        runBlockingTest {
            val expect = listOf(fakeArtist)
            whenever(dsMock.getFavouriteArtists()).thenReturn(expect)

            val result = dsMock.getFavouriteArtists()

            assertEquals(result, expect)
        }
    }

    @Test
    fun artistExists() {
        runBlockingTest {
            val expect = true
            whenever(dsMock.artistExists(fakeArtist.id)).thenReturn(expect)

            val result = dsMock.artistExists(fakeArtist.id)

            assertEquals(expect, result)
        }
    }

    @Test
    fun `artistDoesn'tExists`() {
        runBlockingTest {
            val expect = false
            whenever(dsMock.artistExists(fakeArtist.id)).thenReturn(expect)

            val result = dsMock.artistExists(fakeArtist.id)

            assertEquals(expect, result)
        }
    }

    @Test
    fun insertArtist() {
        runBlockingTest {
            val expect = fakeArtist.id
            whenever(dsMock.insertArtist(fakeArtist)).thenReturn(fakeArtist.id)

            val result = dsMock.insertArtist(fakeArtist)

            assertEquals(expect, result)
        }
    }

    @Test
    fun deleteArtist() {
        runBlockingTest {
            val expectExists = true
            val expectNotExists = true
            val fakeId: Long = 2
            whenever(dsMock.artistExists(fakeArtist.id)).thenReturn(expectExists)
            whenever(dsMock.artistExists(fakeId)).thenReturn(expectNotExists)

            val resultExists = dsMock.artistExists(fakeArtist.id)
            val resultNotExists = dsMock.artistExists(fakeId)

            assertEquals(expectExists, resultExists)
            assertEquals(expectNotExists, resultNotExists)
        }
    }

    @Test
    fun artistHasSongs() {
        runBlockingTest {
            val expect = true
            whenever(dsMock.artistHasSongs(fakeArtist.id)).thenReturn(expect)

            val result = dsMock.artistHasSongs(fakeArtist.id)

            assertEquals(expect, result)
        }
    }

    @Test
    fun countArtistSongs() {
        runBlockingTest {
            val expect = 1
            whenever(dsMock.countArtistSongs(fakeArtist.id)).thenReturn(expect)

            val result = dsMock.countArtistSongs(fakeArtist.id)

            assertEquals(expect, result)
        }
    }

    @Test
    fun getSongs() {
        runBlockingTest {
            val expect = listOf(fakeSong)
            whenever(dsMock.getSongs()).thenReturn(expect)

            val result = dsMock.getSongs()

            assertEquals(expect, result)
        }
    }

    @Test
    fun insertSong() {
        runBlockingTest {
            val expect = fakeSong.id
            whenever(dsMock.insertSong(fakeSong)).thenReturn(fakeSong.id)

            val result = dsMock.insertSong(fakeSong)

            assertEquals(expect, result)
        }
    }
}