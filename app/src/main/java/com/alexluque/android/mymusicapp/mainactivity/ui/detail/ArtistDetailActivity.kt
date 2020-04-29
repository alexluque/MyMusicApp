package com.alexluque.android.mymusicapp.mainactivity.ui.detail

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
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityArtistDetailBinding
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.RoomDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.DeezerMusicoveryDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.GoogleMapsDataSource
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailViewModel.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.ui.main.LocationRecommendationsListener
import com.alexluque.android.mymusicapp.mainactivity.ui.recommendations.RecommendationsActivity
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.data.repositories.GeolocationRepository
import com.example.android.domain.Song
import com.example.android.usecases.GetCountry
import com.example.android.usecases.HandleFavourite
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.app_actionbar.view.*

@Suppress("UNCHECKED_CAST")
class ArtistDetailActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val artistName: String? by lazy { intent.getStringExtra(ARTIST_NAME) }

    private lateinit var binding: ActivityArtistDetailBinding
    private lateinit var viewModel: ArtistDetailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ArtistDetailAdapter

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_detail)
        setSupportActionBar(binding.appBarLayoutToolbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setViewModel(artistName)
        setAdapter()

        ConnectivityController.view = mainView

        observeSongs()
        observeFavourite()
        observeArtistName()
        observeRecommendation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_appbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            SearchArtistFragment(viewModel::loadData)
                .show(supportFragmentManager, FRAGMENT_NAME)
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

    private fun setViewModel(artistName: String?) {
        viewModel = ViewModelProvider(
            this,
            ArtistDetailViewModelFactory(
                artistName,
                HandleFavourite(
                    ArtistDetailRepository(
                        DeezerMusicoveryDataSource(),
                        RoomDataSource(FavouritesRoomDatabase.getDatabase(applicationContext))
                    )
                ),
                GetCountry(GeolocationRepository(GoogleMapsDataSource()))
            )
        ).get(ArtistDetailViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

    @ExperimentalStdlibApi
    private fun setAdapter() {
        viewAdapter = ArtistDetailAdapter(mutableListOf<Song>(), viewModel::onFavouriteClicked, viewModel::isFavourite)

        recyclerView = binding.artistDetailRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun observeSongs() =
        viewModel.songs.observe(
            this,
            Observer { it?.let { viewAdapter.updateData(viewAdapter.songs as MutableList<Any>, it) } }
        )

    private fun observeFavourite() =
        viewModel.favourite.observe(
            this,
            EventObserver {
                val resource = if (it.newFavourite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star
                val msg = if (it.newFavourite) getString(R.string.fav_song_added) else getString(R.string.fav_song_removed)

                it.star.setImageResource(resource)
                it.star.makeLongSnackbar("${it.songName} $msg")
            }
        )

    private fun observeArtistName() =
        viewModel.artistDetailName.observe(
            this,
            Observer {
                if (it == null) {
                    mainView.makeLongSnackbar(getString(R.string.artist_not_found))
                } else {
                    this.title = it.name
                }
            }
        )

    private fun observeRecommendation() =
        viewModel.country.observe(this, EventObserver {
            this.myStartActivity(
                RecommendationsActivity::class.java,
                listOf(AlarmClock.EXTRA_MESSAGE to it)
            )
        })
}
