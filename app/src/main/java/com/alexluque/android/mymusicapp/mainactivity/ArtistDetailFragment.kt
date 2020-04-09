package com.alexluque.android.mymusicapp.mainactivity

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.ArtistDetailViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.databinding.FragmentArtistDetailBinding
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer.SongData
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.ArtistDetailAdapter
import kotlinx.android.synthetic.main.fragment_artist_detail.*

@Suppress("UNCHECKED_CAST")
class ArtistDetailFragment : Fragment() {

    private lateinit var viewModel: ArtistDetailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ArtistDetailAdapter

    private var binding: FragmentArtistDetailBinding? = null
    private val args: ArtistDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_artist_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val artistName = args.artistName
        activity?.title = if (artistName.isNullOrEmpty()) getString(R.string.artists_name) else artistName

        setViewModel(artistName)
        setAdapter()

        search_button.setOnClickListener {
            activity?.let { SearchArtistFragment(viewModel::loadData).show(it.supportFragmentManager, FRAGMENT_NAME) }
        }

        observeSongs()
        observeFavourite()
        observeCurrentArtist()
    }

    private fun setViewModel(artistName: String?) {
        activity?.let {
            viewModel = ViewModelProvider(
                this,
                ArtistDetailViewModelFactory(artistName, it.application)
            ).get(ArtistDetailViewModel::class.java)
        }

        binding?.viewmodel = viewModel
        binding?.lifecycleOwner = this
    }

    private fun setAdapter() {
        viewAdapter = ArtistDetailAdapter(mutableListOf<SongData>(), viewModel::onFavouriteClicked, viewModel::isFavourite)

        recyclerView = artist_detail_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun observeSongs() =
        viewModel.songs.observe(
            viewLifecycleOwner,
            Observer { it?.let { viewAdapter.updateData(viewAdapter.songs as MutableList<Any>, it) } }
        )

    private fun observeFavourite() =
        viewModel.favourite.observe(
            viewLifecycleOwner,
            EventObserver {
                val resource = if (it.newFavourite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star
                val msg = if (it.newFavourite) getString(R.string.fav_song_added) else getString(R.string.fav_song_removed)

                it.star.setImageResource(resource)
                it.star.makeLongSnackbar("${it.songName} $msg")
            }
        )

    private fun observeCurrentArtist() =
        viewModel.currentArtist.observe(
            viewLifecycleOwner,
            Observer { if (it == null) view?.makeLongSnackbar(getString(R.string.artist_not_found)) }
        )
}
