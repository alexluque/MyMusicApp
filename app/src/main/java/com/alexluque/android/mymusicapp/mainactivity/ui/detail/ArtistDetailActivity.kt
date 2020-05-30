package com.alexluque.android.mymusicapp.mainactivity.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityArtistDetailBinding
import com.alexluque.android.mymusicapp.mainactivity.di.ListenersModule
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.common.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.*
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailViewModel.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.example.android.domain.Song
import com.google.android.gms.location.LocationServices


@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
class ArtistDetailActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val artistName: String? by lazy { intent.getStringExtra(ARTIST_NAME) }
    private val viewModel: ArtistDetailViewModel by lazy { getViewModel { component.detailViewModel } }

    private lateinit var binding: ActivityArtistDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ArtistDetailAdapter
    private lateinit var component: DetailActivityComponent

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist_detail)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.setStatusBarColor(R.color.colorPrimaryDark)

        component = app.component.plus(
            DetailActivityModule(),
            ListenersModule(this, LocationServices.getFusedLocationProviderClient(this))
        )

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setAdapter()

        ConnectivityController.getInstance().view = mainView

        observeSongs()
        observeFavourite()
        observeArtistName()

        viewModel.loadData(RetrofitBuilder, artistName)
        viewModel.loadFavouriteSongs()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_appbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            SearchArtistFragment(viewModel::loadData, viewModel::loadFavouriteSongs)
                .show(supportFragmentManager, FRAGMENT_NAME)
            true
        }
        R.id.action_recommend -> {
            addLocationPermission(component.locationListener)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @ExperimentalStdlibApi
    private fun setAdapter() {
        viewAdapter = ArtistDetailAdapter(mutableListOf<Song>(), viewModel::onFavouriteClicked, viewModel::isFavouriteSong)

        recyclerView = binding.artistDetailRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun observeSongs() =
        viewModel.songs.observe(
            this,
            Observer {
                it?.let { viewModel.updateDetail(viewAdapter, it, binding.appBarLayoutImage, recyclerView) }
            }
        )

    private fun observeFavourite() =
        viewModel.favourite.observe(
            this,
            EventObserver {
                val resource = if (it.newFavourite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star
                val msg = if (it.newFavourite) getString(R.string.fav_song_added) else getString(R.string.fav_song_removed)

                it.star.setImageResource(resource)
                mainView.makeLongSnackbar("${it.songName} $msg")
            }
        )

    private fun observeArtistName() =
        viewModel.artistDetailName.observe(
            this,
            Observer {
                if (it == null) {
                    mainView.makeLongSnackbar(getString(R.string.artist_not_found))
                } else {
                    this.title = it
                }
            }
        )
}
