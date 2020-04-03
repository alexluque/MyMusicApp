package com.alexluque.android.mymusicapp.mainactivity

import android.Manifest
import android.os.Bundle
import android.provider.AlarmClock
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.LocationRecommendationsListener
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.runIfNotHandled
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.MainActivityViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.MainActivityViewModel.UiModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.MainActivityViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.FavouriteArtistsAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val recommendButton: Button by lazy { recommend_button }
    private val recommendFloatingBtn: FloatingActionButton by lazy { recommend_floating_btn }
    private val fusedClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val searchButton: FloatingActionButton by lazy { search_button }
    private val progress: ProgressBar by lazy { progressBar }

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FavouriteArtistsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        this.title = getString(R.string.main_activity_title)

        viewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(application)
        ).get(MainActivityViewModel::class.java)

        viewAdapter = FavouriteArtistsAdapter(listOf<Artist>(), viewModel::onArtistClicked)

        recyclerView = artists_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        ConnectivityController.registerCallback(this, mainView)
        setOnClickListeners()

        viewModel.model.observe(this, Observer(::updateUi))
        observeNavigation()
        observeRecommendation()
        observeSearch()
    }

    private fun updateUi(model: UiModel) {
        progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is UiModel.Content -> viewAdapter.artists = model.artists
        }
    }

    private fun observeSearch() =
        this.runIfNotHandled(viewModel.search) {
            SearchArtistFragment().show(supportFragmentManager, FRAGMENT_NAME)
        }

    private fun observeRecommendation() =
        this.runIfNotHandled(viewModel.recommendation) {
            this.myStartActivity(
                RecommendationsActivity::class.java,
                listOf(AlarmClock.EXTRA_MESSAGE to (it as UiModel.Recommendations).country)
            )
        }

    private fun observeNavigation() =
        this.runIfNotHandled(viewModel.navigation) {
            this.myStartActivity(
                ArtistDetailActivity::class.java,
                listOf(ArtistDetailViewModel.ARTIST_NAME to (it as UiModel.Navigation).artistName)
            )
        }

    override fun onResume() {
        super.onResume()
        ConnectivityController.view = mainView
    }

    private fun setOnClickListeners() {
        recommendButton.setOnClickListener { addLocationPermission() }
        recommendFloatingBtn.setOnClickListener { addLocationPermission() }
        searchButton.setOnClickListener {
            viewModel.onSearchClicked()
        }
    }

    private fun addLocationPermission() =
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(
                LocationRecommendationsListener(
                    getString(R.string.google_maps_key),
                    viewModel::onRecommendClicked,
                    fusedClient)
            ).check()

}