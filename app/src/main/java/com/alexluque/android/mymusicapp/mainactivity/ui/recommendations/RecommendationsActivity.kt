package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
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
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityRecommendationsBinding
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.RoomDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.DeezerMusicoveryDataSource
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.ui.main.LocationRecommendationsListener
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment
import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.data.repositories.RecommendedArtistsRepository
import com.example.android.domain.RecommendedArtist
import com.example.android.usecases.GetArtistDetail
import com.example.android.usecases.GetRecommendedArtists
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_recommendations.*
import kotlinx.android.synthetic.main.app_actionbar.view.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class RecommendationsActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val countryName: String by lazy { intent.getStringExtra(EXTRA_MESSAGE) }

    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var viewAdapter: RecommendedArtistsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityRecommendationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recommendations)
        setSupportActionBar(binding.appBarLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        invalidateOptionsMenu() // Forces to redraw the layout. Needed when some change is made like hide an icon.

        ConnectivityController.view = mainView

        setViewModel()
        setAdapter()
        observeArtists()
        observeDetail()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_appbar, menu)
        menu.findItem(R.id.action_recommend).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            SearchArtistFragment().show(supportFragmentManager, SearchArtistFragment.FRAGMENT_NAME)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setViewModel() {
        val remoteDS = DeezerMusicoveryDataSource()

        viewModel = ViewModelProvider(
            this,
            RecommendationsViewModelFactory(
                countryName,
                getString(R.string.recommendations_activity_title),
                GetArtistDetail(
                    ArtistDetailRepository(
                        remoteDS,
                        RoomDataSource(FavouritesRoomDatabase.getDatabase(applicationContext))
                    )
                ),
                GetRecommendedArtists(RecommendedArtistsRepository(remoteDS))
            )
        ).get(RecommendationsViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setAdapter() {
        viewAdapter = RecommendedArtistsAdapter(mutableListOf<RecommendedArtist>(), viewModel::onArtistClicked, viewModel::loadImage)

        recyclerView = recommended_artists_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun observeArtists() =
        viewModel.artists.observe(
            this,
            Observer {
                val artists = viewAdapter.artists as MutableList<Any>
                viewAdapter.updateData(artists, it)
                val msg = if (artists.isEmpty())
                    getString(R.string.no_recommended_artists_found)
                else
                    this.getString(R.string.country_recommendations) + " ${countryName.toUpperCase(Locale.ROOT)}"
                mainView.makeLongSnackbar(msg)
            }
        )

    private fun observeDetail() =
        viewModel.detail.observe(this, EventObserver {
            this.myStartActivity(
                ArtistDetailActivity::class.java,
                listOf(ArtistDetailViewModel.ARTIST_NAME to it)
            )
        })
}
