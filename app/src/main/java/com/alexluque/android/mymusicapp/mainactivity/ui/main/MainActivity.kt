package com.alexluque.android.mymusicapp.mainactivity.ui.main

import android.os.Bundle
import android.provider.AlarmClock
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.addLocationPermission
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityMainBinding
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.RoomDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.GoogleMapsDataSource
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.ui.recommendations.RecommendationsActivity
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.example.android.data.repositories.FavouriteArtistsRepository
import com.example.android.data.repositories.GeolocationRepository
import com.example.android.domain.FavouriteArtist
import com.example.android.usecases.GetCountry
import com.example.android.usecases.GetFavouriteArtistSongs
import com.example.android.usecases.GetFavouriteArtists
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.app_actionbar.view.*

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FavouriteArtistsAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.appBarLayout.toolbar)

        setViewModel()
        setAdapter()

        viewModel.artists.observe(this, Observer(::observeContent))
        observeSearch()
        observeRecommendation()

        ConnectivityController.registerCallback(this, mainView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_appbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            SearchArtistFragment().show(supportFragmentManager, FRAGMENT_NAME)
            true
        }
        R.id.action_recommend -> {
            addLocationPermission(
                LocationRecommendationsListener(
                    getString(R.string.google_maps_key),
                    viewModel::onRecommendClicked,
                    LocationServices.getFusedLocationProviderClient(this)
                )
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityController.view = mainView
        viewModel.loadArtists()
    }

    private fun setViewModel() {
        val repository = FavouriteArtistsRepository(RoomDataSource(FavouritesRoomDatabase.getDatabase(applicationContext)))

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(
                GetFavouriteArtists(repository),
                GetFavouriteArtistSongs(repository),
                GetCountry(GeolocationRepository(GoogleMapsDataSource()))
            )
        ).get(MainViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setAdapter() {
        viewAdapter = FavouriteArtistsAdapter(mutableListOf<FavouriteArtist>(), viewModel::onArtistClicked)

        recyclerView = binding.artistsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun observeContent(artists: List<FavouriteArtist>) =
        viewAdapter.updateData(viewAdapter.artists as MutableList<Any>, artists)

    private fun observeRecommendation() =
        viewModel.country.observe(this, EventObserver {
            this.myStartActivity(
                RecommendationsActivity::class.java,
                listOf(AlarmClock.EXTRA_MESSAGE to it)
            )
        })

    private fun observeSearch() =
        viewModel.artistName.observe(this, EventObserver {
            this.myStartActivity(
                ArtistDetailActivity::class.java,
                listOf(ArtistDetailViewModel.ARTIST_NAME to it)
            )
        })

}