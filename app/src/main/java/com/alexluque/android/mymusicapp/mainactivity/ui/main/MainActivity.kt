package com.alexluque.android.mymusicapp.mainactivity.ui.main

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
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityMainBinding
import com.alexluque.android.mymusicapp.mainactivity.di.ListenersModule
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.common.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.*
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.alexluque.android.domain.FavouriteArtist
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.app_actionbar.view.*

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FavouriteArtistsAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var component: MainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.appBarLayout.toolbar)

        component = app.component.plus(
            MainActivityModule(),
            ListenersModule(this, LocationServices.getFusedLocationProviderClient(this))
        )

        viewModel = getViewModel { component.mainViewModel }
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setAdapter()

        viewModel.artists.observe(this, Observer(::observeContent))
        observeSearch()

        ConnectivityController.getInstance().registerCallback(this, mainView)
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
            addLocationPermission(component.locationLister)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityController.getInstance().view = mainView
        viewModel.loadArtists()
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

    private fun observeSearch() =
        viewModel.artistName.observe(this, EventObserver {
            this.myStartActivity(
                ArtistDetailActivity::class.java,
                listOf(ArtistDetailViewModel.ARTIST_NAME to it)
            )
        })

}