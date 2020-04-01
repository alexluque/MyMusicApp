package com.alexluque.android.mymusicapp.mainactivity

import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.MusicoveryArtist
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.myStartActivity
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.runIfNotHandled
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModel.UiModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.RecommendedArtistsAdapter
import kotlinx.android.synthetic.main.activity_recommendations.*
import java.util.*

class RecommendationsActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val countryName: String by lazy { intent.getStringExtra(EXTRA_MESSAGE) }
    private val progress: ProgressBar by lazy { recommendations_progressBar }

    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var viewAdapter: RecommendedArtistsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)
        this.title = getString(R.string.recommendations_activity_title)

        ConnectivityController.view = mainView

        viewModel = ViewModelProvider(this, RecommendationsViewModelFactory(countryName))
            .get(RecommendationsViewModel::class.java)

        viewAdapter = RecommendedArtistsAdapter(listOf<MusicoveryArtist>(), viewModel::onArtistClicked, viewModel::loadImage)

        recyclerView = recommended_artists_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewModel.model.observe(this, Observer(::updateUi))
        observeNavigation()
    }

    private fun updateUi(model: UiModel) {
        progress.visibility = if (model == UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is UiModel.Content -> {
                viewAdapter.artists = model.artists
                mainView.makeLongSnackbar(this.getString(R.string.country_recommendations) + " ${countryName.toUpperCase(Locale.ROOT)}")
            }
            is UiModel.Navigation -> this.myStartActivity(
                ArtistDetailActivity::class.java,
                listOf(ArtistDetailViewModel.ARTIST_NAME to model.artistName)
            )
        }
    }

    private fun observeNavigation() =
        this.runIfNotHandled(viewModel.navigation) {
            this.myStartActivity(
                ArtistDetailActivity::class.java,
                listOf(ArtistDetailViewModel.ARTIST_NAME to (it as UiModel.Navigation).artistName)
            )
        }
}
