package com.alexluque.android.mymusicapp.mainactivity.ui.recommendations

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.ui.common.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.ui.common.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityRecommendationsBinding
import com.alexluque.android.mymusicapp.mainactivity.model.network.RetrofitBuilder
import com.alexluque.android.mymusicapp.mainactivity.ui.common.extensions.*
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailActivity
import com.alexluque.android.mymusicapp.mainactivity.ui.detail.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.ui.main.LocationRecommendationsListener.Companion.LATITUDE
import com.alexluque.android.mymusicapp.mainactivity.ui.main.LocationRecommendationsListener.Companion.LONGITUDE
import com.alexluque.android.mymusicapp.mainactivity.ui.search.SearchArtistFragment
import com.example.android.domain.RecommendedArtist
import kotlinx.android.synthetic.main.activity_recommendations.*
import kotlinx.android.synthetic.main.app_actionbar.view.*
import java.util.*

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
class RecommendationsActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val latitude: String by lazy { intent.getStringExtra(LATITUDE) }
    private val longitude: String by lazy { intent.getStringExtra(LONGITUDE) }

    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var viewAdapter: RecommendedArtistsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityRecommendationsBinding
    private lateinit var component: RecommendationsActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recommendations)
        setSupportActionBar(binding.appBarLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        invalidateOptionsMenu() // Forces to redraw the layout. Needed when some change is made like hide an icon.

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        component = app.component.plus(RecommendationsActivityModule(latitude, longitude, getString(R.string.google_maps_key)))

        viewModel = getViewModel { component.recommendationsViewModel }

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        ConnectivityController.getInstance().view = mainView

        setAdapter()
        observeArtists()
        observeDetail()
        observeCountry()
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
                    this.getString(R.string.country_recommendations) +
                            " ${viewModel.country.value?.peekContent()?.toUpperCase(Locale.ROOT)}"

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

    private fun observeCountry() =
        viewModel.country.observe(this, EventObserver { viewModel.loadRecommendations(RetrofitBuilder.musicoveryInstance) })
}
