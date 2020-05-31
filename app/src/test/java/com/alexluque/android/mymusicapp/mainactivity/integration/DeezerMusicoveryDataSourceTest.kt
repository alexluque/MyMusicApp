package com.alexluque.android.mymusicapp.mainactivity.integration

import com.alexluque.android.mymusicapp.mainactivity.model.emptyDomainArtist
import com.alexluque.android.mymusicapp.mainactivity.model.network.DeezerMusicoveryDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.*
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.*
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.DeezerArtistService
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MusicoveryArtistService
import com.example.android.domain.ArtistDetail
import com.example.android.domain.RecommendedArtist
import com.example.android.domain.Artist
import com.google.gson.internal.LinkedTreeMap
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.Artist as DeezerArtist
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.Artist as MusicoveryArtist
import com.example.android.domain.Artist as DomainArtist
import com.example.android.domain.Song as DomainSong

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeezerMusicoveryDataSourceTest {

    private val artistName = "artist"
    private val country = "country"
    private val mbid = "mbid"
    private val genre = "genre"

    private val deezerArtistServiceMock = Mockito.mock(DeezerArtistService::class.java)
    private val musicoveryArtistServiceMock = Mockito.mock(MusicoveryArtistService::class.java)

    @Spy
    private lateinit var dataSourceMock: DeezerMusicoveryDataSource

    @Spy
    private val retrofitDeezerStub = RetrofitBuilder.deezerInstance

    @Spy
    private val retrofitMusicoveryStub = RetrofitBuilder.musicoveryInstance

    @Mock
    private lateinit var artistStub: DeezerArtist

    @Mock
    private lateinit var songsDataStub: SongsData

    @Mock
    private lateinit var artistsByLocationStub: GetArtistsByLocationResponse

    @Mock
    private lateinit var artistByNameStub: GetArtistByNameResponse

    @Mock
    private lateinit var getArtistResponseStub: GetArtistResponse

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Artist detail retrieved from given name`() {
        runBlockingTest {
            val fakeId: Long = 1
            val fakeBigUrl = "big"
            val fakeMediumUrl = "medium"
            val artistDataStub = Mockito.mock(ArtistData::class.java)

            whenever(artistDataStub.id).thenReturn(fakeId)
            whenever(artistDataStub.name).thenReturn(artistName)
            whenever(artistDataStub.picture_big).thenReturn(fakeBigUrl)
            whenever(artistDataStub.picture_medium).thenReturn(fakeMediumUrl)
            whenever(retrofitDeezerStub.create(DeezerArtistService::class.java)).thenReturn(deezerArtistServiceMock)
            whenever(deezerArtistServiceMock.getArtist(artistName)).thenReturn(artistStub)
            whenever(artistStub.data).thenReturn(listOf(artistDataStub))

            val expect = ArtistDetail(fakeId, artistName, fakeBigUrl, fakeMediumUrl)
            val result = dataSourceMock.getArtistDetail(retrofitDeezerStub, artistName)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `No artist detail retrieved from given name`() {
        runBlockingTest {
            whenever(retrofitDeezerStub.create(DeezerArtistService::class.java)).thenReturn(deezerArtistServiceMock)
            whenever(deezerArtistServiceMock.getArtist(artistName)).thenReturn(artistStub)
            whenever(artistStub.data).thenReturn(null)

            val expect = null
            val result = dataSourceMock.getArtistDetail(retrofitDeezerStub, artistName)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Song list retrieved from given artist`() {
        runBlockingTest {
            val songDataStub = Mockito.mock(SongData::class.java)
            val albumDataMock = Mockito.mock(AlbumData::class.java)
            val artistSongData = Mockito.mock(ArtistSongData::class.java)
            val fakeSongId: Long = 1
            val fakeTitle = "title"
            val fakeAlbumName = "album"
            val fakeArtistId = 2

            whenever(retrofitDeezerStub.create(DeezerArtistService::class.java)).thenReturn(deezerArtistServiceMock)
            whenever(deezerArtistServiceMock.getSongs(artistName)).thenReturn(songsDataStub)
            whenever(songDataStub.id).thenReturn(fakeSongId)
            whenever(songDataStub.title).thenReturn(fakeTitle)
            whenever(albumDataMock.title).thenReturn(fakeAlbumName)
            whenever(songDataStub.album).thenReturn(albumDataMock)
            whenever(songsDataStub.data).thenReturn(listOf(songDataStub))
            whenever(artistSongData.id).thenReturn(fakeArtistId)
            whenever(songDataStub.artistSongData).thenReturn(artistSongData)

            val expect = listOf(DomainSong(fakeSongId, fakeTitle, fakeAlbumName, fakeArtistId.toLong()))
            val result = dataSourceMock.getSongs(retrofitDeezerStub, artistName)

            val apiRetrievedArtistId = songsDataStub.data.first().artistSongData?.id?.toLong()
            val mappedArtistId = result.first().artistId
            Assert.assertEquals(expect, result)
            Assert.assertEquals(apiRetrievedArtistId, mappedArtistId)
        }
    }

    @Test
    fun `No songs retrieved from given artist`() {
        runBlockingTest {
            whenever(retrofitDeezerStub.create(DeezerArtistService::class.java)).thenReturn(deezerArtistServiceMock)
            whenever(deezerArtistServiceMock.getSongs(artistName)).thenReturn(songsDataStub)
            whenever(songsDataStub.data).thenReturn(listOf<SongData>())

            val expect = listOf<DomainSong>()
            val result = dataSourceMock.getSongs(retrofitDeezerStub, artistName)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Songs are ordered by its album id`() {
        runBlockingTest {
            val songId1: Long = 1
            val songId2: Long = 2
            val songId3: Long = 3
            val title1 = "title 1"
            val title2 = "title 2"
            val title3 = "title 3"

            whenever(retrofitDeezerStub.create(DeezerArtistService::class.java)).thenReturn(deezerArtistServiceMock)
            whenever(deezerArtistServiceMock.getSongs(artistName)).thenReturn(songsDataStub)
            whenever(songsDataStub.data).thenReturn(
                listOf(
                    SongData(id = songId1, title = title1, album = AlbumData(id = 1)),
                    SongData(id = songId2, title = title2, album = AlbumData(id = 2)),
                    SongData(id = songId3, title = title3)
                )
            )

            val expect = listOf(
                DomainSong(songId3, title3, null, null),
                DomainSong(songId1, title1, artistId = null),
                DomainSong(songId2, title2, artistId = null)
            )
            val result = dataSourceMock.getSongs(retrofitDeezerStub, artistName)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Get list of artists from given location`() {
        runBlockingTest {
            val artistMock = Mockito.mock(MusicoveryArtist::class.java)
            val artistsMock = Mockito.mock(Artists::class.java)
            val fakeGenre: Any? = "genre"

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtistsByLocation(country.toLowerCase(Locale.ROOT).trim())).thenReturn(artistsByLocationStub)
            whenever(artistsByLocationStub.artists).thenReturn(artistsMock)
            whenever(artistMock.name).thenReturn(artistName)
            whenever(artistMock.genres).thenReturn(fakeGenre)
            whenever(artistMock.country).thenReturn(country)
            whenever(artistsMock.artist).thenReturn(listOf(artistMock))

            val recommendedArtist = RecommendedArtist(artistName, fakeGenre?.toString(), country)
            val expect = listOf(recommendedArtist)
            val result = dataSourceMock.getArtistsByLocation(retrofitMusicoveryStub, country)

            val countryArtistRetrievedFromAPI = result.first().country!!.toString()
            val countryArtistMappedFromAPI = artistMock.country.toString()
            Assert.assertEquals(expect, result)
            Assert.assertTrue(countryArtistRetrievedFromAPI.equals(countryArtistMappedFromAPI, true))
        }
    }

    @Test
    fun `Get empty list when NPE is thrown while retrieving recommended artists`() {
        runBlockingTest {
            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtistsByLocation(country.toLowerCase(Locale.ROOT).trim())).thenReturn(artistsByLocationStub)
            whenever(artistsByLocationStub.artists).thenThrow(NullPointerException::class.java)

            val expect = listOf<RecommendedArtist>()
            val result = dataSourceMock.getArtistsByLocation(retrofitMusicoveryStub, country)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Map instance retrieved from API containing values and no exception occurred, DomainArtist returned`() {
        runBlockingTest {
            val thirdLevel = LinkedTreeMap<String, String>()
            val secondLevel = mapOf(1 to thirdLevel)
            val firstLevel = mapOf(1 to secondLevel)
            val fakeArtists = mapOf(1 to firstLevel)
            thirdLevel[mbid] = mbid
            thirdLevel[artistName] = artistName
            thirdLevel[genre] = genre
            thirdLevel[country] = country

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtist(artistName)).thenReturn(artistByNameStub)
            whenever(artistByNameStub.artists).thenReturn(fakeArtists)

            val result = dataSourceMock.getArtist(retrofitMusicoveryStub, artistName)
            val expect = DomainArtist(mbid, artistName, genre, country)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Map instance retrieved from API containing values and first ClassCastException occurred, DomainArtist returned`() {
        runBlockingTest {
            val forthLevel = LinkedTreeMap<String, String>()
            val thirdLevel = mapOf(1 to forthLevel)
            val secondLevel = mapOf(1 to thirdLevel)
            val firstLevel = listOf(secondLevel)
            val fakeArtists = mapOf(1 to firstLevel)
            forthLevel[mbid] = mbid
            forthLevel[artistName] = artistName
            forthLevel[genre] = genre
            forthLevel[country] = country

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtist(artistName)).thenReturn(artistByNameStub)
            whenever(artistByNameStub.artists).thenReturn(fakeArtists)

            val result = dataSourceMock.getArtist(retrofitMusicoveryStub, artistName)
            val expect = DomainArtist(mbid, artistName, genre, country)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Map instance retrieved from API containing unknown format values and second ClassCastException occurred, empty object returned`() {
        runBlockingTest {
            val firstLevel = Object()
            val fakeArtists = mapOf(1 to firstLevel)

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtist(artistName)).thenReturn(artistByNameStub)
            whenever(artistByNameStub.artists).thenReturn(fakeArtists)

            val result = dataSourceMock.getArtist(retrofitMusicoveryStub, artistName)
            val expect = emptyDomainArtist()

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Empty Map instance retrieved from API, empty object returned`() {
        runBlockingTest {
            val fakeArtists = emptyMap<Any, Any>()

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtist(artistName)).thenReturn(artistByNameStub)
            whenever(artistByNameStub.artists).thenReturn(fakeArtists)

            val result = dataSourceMock.getArtist(retrofitMusicoveryStub, artistName)
            val expect = emptyDomainArtist()

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `ArtistsByNameResponse retrieved from API, DomainArtist returned`() {
        runBlockingTest {
            val fakeArtists = ArtistsByNameResponse(
                ArtistByNameResponse(
                    ArtistsByNameResponse2(
                        MusicoveryArtist(mbid, artistName, genre, country)
                    ),
                    1
                )
            )

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtist(artistName)).thenReturn(artistByNameStub)
            whenever(artistByNameStub.artists).thenReturn(fakeArtists)

            val result = dataSourceMock.getArtist(retrofitMusicoveryStub, artistName)
            val expect = DomainArtist(mbid, artistName, genre, country)

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Unknown response from API, empty object returned`() {
        runBlockingTest {
            val fakeArtists = Object()

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtist(artistName)).thenReturn(artistByNameStub)
            whenever(artistByNameStub.artists).thenReturn(fakeArtists)

            val result = dataSourceMock.getArtist(retrofitMusicoveryStub, artistName)
            val expect = emptyDomainArtist()

            Assert.assertEquals(expect, result)
        }
    }

    @Test
    fun `Artist info retrieved`() {
        runBlockingTest {
            val region = "region"
            val artistInfo = MusicoveryArtist(artistName, mbid, genre, country, region)

            whenever(retrofitMusicoveryStub.create(MusicoveryArtistService::class.java)).thenReturn(musicoveryArtistServiceMock)
            whenever(musicoveryArtistServiceMock.getArtistInfo(mbid)).thenReturn(getArtistResponseStub)
            whenever(getArtistResponseStub.artist).thenReturn(artistInfo)

            val result = dataSourceMock.getArtistInfo(retrofitMusicoveryStub, mbid)
            val expect = Artist(mbid, artistName, genre, country, region)

            Assert.assertEquals(expect, result)
        }
    }
}