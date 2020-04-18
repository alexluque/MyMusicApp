package com.alexluque.android.mymusicapp.mainactivity

import android.os.Bundle
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
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel.Companion.ARTIST_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.databinding.ActivityArtistDetailBinding
import com.alexluque.android.mymusicapp.mainactivity.model.database.FavouritesRoomDatabase
import com.alexluque.android.mymusicapp.mainactivity.model.database.RoomDataSource
import com.alexluque.android.mymusicapp.mainactivity.model.network.DeezerMusicoveryDataSource
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.ArtistDetailAdapter
import com.example.android.data.repositories.ArtistDetailRepository
import com.example.android.domain.Song
import com.example.android.usecases.HandleFavourite
import kotlinx.android.synthetic.main.activity_artist_detail.*

@Suppress("UNCHECKED_CAST")
class ArtistDetailActivity : AppCompatActivity() {

    private val mainView: View by lazy { findViewById<View>(android.R.id.content) }
    private val artistName: String? by lazy { intent.getStringExtra(ARTIST_NAME) }

    private lateinit var viewModel: ArtistDetailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ArtistDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)

        setViewModel(artistName)
        setAdapter()

        ConnectivityController.view = mainView

        search_button.setOnClickListener {
            SearchArtistFragment(viewModel::loadData).show(supportFragmentManager, FRAGMENT_NAME)
        }

        observeSongs()
        observeFavourite()
        observeCurrentArtist()
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
                )
            )
        ).get(ArtistDetailViewModel::class.java)

        val binding: ActivityArtistDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist_detail)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setAdapter() {
        viewAdapter = ArtistDetailAdapter(mutableListOf<Song>(), viewModel::onFavouriteClicked, viewModel::isFavourite)

        recyclerView = artist_detail_recyclerView.apply {
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

    private fun observeCurrentArtist() =
        viewModel.currentArtist.observe(
            this,
            Observer {
                if (it == null) {
                    mainView.makeLongSnackbar(getString(R.string.artist_not_found))
                    this.title = getString(R.string.artists_name)
                } else {
                    this.title = it.name
                }
            }
        )
}
