package com.alexluque.android.mymusicapp.mainactivity

import SongData
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.loadImage
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.runIfNotHandled
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel.UiModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.ArtistDetailAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_artist_detail.*

class ArtistDetailActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val progress: ProgressBar by lazy { artist_progressBar }
    private val artistImage: ImageView by lazy { artist_image }
    private val artistNameView: TextView by lazy { artist_name }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val searchButton: FloatingActionButton by lazy { search_button }

    private lateinit var viewModel: ArtistDetailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ArtistDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)

        val artistName = intent.getStringExtra(ARTIST_NAME)
        viewModel = ViewModelProvider(this, ArtistDetailViewModelFactory(artistName))
            .get(ArtistDetailViewModel::class.java)

        viewAdapter = ArtistDetailAdapter(listOf<SongData>())

        recyclerView = artist_detail_recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        ConnectivityController.view = mainView
        searchButton.setOnClickListener { viewModel.onSearchClicked() }

        viewModel.model.observe(this, Observer(::updateUi))
        observeSearch()
    }

    private fun updateUi(model: UiModel) {
        progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is UiModel.Content -> {
                if (model.artist != null) {
                    artistNameView.text = model.artist.name
                    artistImage.loadImage(model.artist.picture_big)
                } else {
                    mainView.makeLongSnackbar(this.getString(R.string.artist_not_found))
                }

                viewAdapter.songs = model.songs
            }
        }
    }

    private fun observeSearch() =
        this.runIfNotHandled(viewModel.search) {
            SearchArtistFragment(viewModel::loadData).show(supportFragmentManager, FRAGMENT_NAME)
        }
}
