package com.alexluque.android.mymusicapp.mainactivity.integration

import com.alexluque.android.mymusicapp.mainactivity.model.network.GoogleMapsDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.geocoding.GeocodingResponse
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.geocoding.Results
import com.alexluque.android.mymusicapp.mainactivity.model.network.services.MapsGeocodingService
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
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GoogleMapsDataSourceTest {

    private val latlng = "latlng"
    private val mapsKey = "key"

    private val serviceMock = Mockito.mock(MapsGeocodingService::class.java)

    @Spy
    private lateinit var dataSourceMock: GoogleMapsDataSource
    @Spy
    private val retrofitMock: Retrofit = RetrofitBuilder.geocodingInstance

    @Mock
    private lateinit var geocodingResponseStub: GeocodingResponse
    @Mock
    private lateinit var resultStub: Results

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Valid address retrieved so country name returned`() {
        runBlockingTest {
            val fakeAddress = "calle bla 12, murcia, spain"
            val expected = "spain"

            whenever(retrofitMock.create(MapsGeocodingService::class.java)).thenReturn(serviceMock)
            whenever(serviceMock.getAddresses(latlng, mapsKey)).thenReturn(geocodingResponseStub)
            whenever(geocodingResponseStub.results).thenReturn(listOf(resultStub))
            whenever(resultStub.formatted_address).thenReturn(fakeAddress)

            val result = dataSourceMock.getCountry(retrofitMock, latlng, mapsKey)

            Assert.assertEquals(expected, result)
        }
    }

    @Test
    fun `Empty list retrieved so empty value returned`() {
        runBlockingTest {
            val expected = ""

            whenever(retrofitMock.create(MapsGeocodingService::class.java)).thenReturn(serviceMock)
            whenever(serviceMock.getAddresses(latlng, mapsKey)).thenReturn(geocodingResponseStub)
            whenever(geocodingResponseStub.results).thenReturn(listOf())

            val result = dataSourceMock.getCountry(retrofitMock, latlng, mapsKey)

            Assert.assertEquals(expected, result)
        }
    }
}