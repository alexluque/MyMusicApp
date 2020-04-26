package com.alexluque.android.mymusicapp.mainactivity

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityRecommendationsBinding
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.RoomDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.DeezerMusicoveryDataSource
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.RecommendedArtistsAdapter
import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.data.repositories.RecommendedArtistsRepository
import com.example.android.domain.RecommendedArtist
import com.example.android.usecases.GetArtistDetail
import com.example.android.usecases.GetRecommendedArtists
import kotlinx.android.synthetic.main.activity_recommendations.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class RecommendationsActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val countryName: String by lazy { intent.getStringExtra(EXTRA_MESSAGE) }

    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var viewAdapter: RecommendedArtistsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        ConnectivityController.view = mainView

        setViewModel()
        setAdapter()
        observeArtists()
        observeDetail()
    }

    private fun setViewModel() {
        val remoteDS = DeezerMusicoveryDataSource()

        viewModel = ViewModelProvider(
            this,
            RecommendationsViewModelFactory(
                countryName,
                GetArtistDetail(
                    ArtistDetailRepository(
                        remoteDS,
                        RoomDataSource(FavouritesRoomDatabase.getDatabase(applicationContext))
                    )
                ),
                GetRecommendedArtists(RecommendedArtistsRepository(remoteDS))
            )
        ).get(RecommendationsViewModel::class.java)

        val binding: ActivityRecommendationsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_recommendations)
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
                viewAdapter.updateData(viewAdapter.artists as MutableList<Any>, it)
                mainView.makeLongSnackbar(this.getString(R.string.country_recommendations) + " ${countryName.toUpperCase(Locale.ROOT)}")
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
