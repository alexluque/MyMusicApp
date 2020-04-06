package com.alexluque.android.mymusicapp.mainactivity

import android.Manifest
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.controller.LocationRecommendationsListener
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.MainActivityViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.MainActivityViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityMainBinding
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.FavouriteArtistsAdapter
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FavouriteArtistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        setViewModel()
        setAdapter()
        setOnClickListeners()

        viewModel.artists.observe(this, Observer(::observeContent))
        observeNavigation()
        observeRecommendation()

        ConnectivityController.registerCallback(this, mainView)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityController.view = mainView
        viewModel.loadArtists()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(application)
        ).get(MainActivityViewModel::class.java)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setAdapter() {
        viewAdapter = FavouriteArtistsAdapter(mutableListOf<Artist>(), viewModel::onArtistClicked)

        recyclerView = artists_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun setOnClickListeners() {
        recommend_button.setOnClickListener { addLocationPermission() }
        recommend_floating_btn.setOnClickListener { addLocationPermission() }
        search_button.setOnClickListener { SearchArtistFragment().show(supportFragmentManager, FRAGMENT_NAME) }
    }

    private fun observeContent(artists: List<Artist>) =
        viewAdapter.updateData(viewAdapter.artists as MutableList<Any>, artists)

    private fun addLocationPermission() =
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(
                LocationRecommendationsListener(
                    getString(R.string.google_maps_key),
                    viewModel::onRecommendClicked,
                    LocationServices.getFusedLocationProviderClient(this)
                )
            ).check()

    private fun observeRecommendation() =
        viewModel.country.observe(this, EventObserver {
            this.myStartActivity(
                RecommendationsActivity::class.java,
                listOf(AlarmClock.EXTRA_MESSAGE to it)
            )
        })

    private fun observeNavigation() =
        viewModel.artistName.observe(this, EventObserver {
            this.myStartActivity(
                ArtistDetailActivity::class.java,
                listOf(ArtistDetailViewModel.ARTIST_NAME to it)
            )
        })

}