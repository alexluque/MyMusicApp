package com.alexluque.android.mymusicapp.mainactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.controller.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.databinding.FragmentRecommendationsBinding
import com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery.MusicoveryArtist
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.RecommendedArtistsAdapter
import kotlinx.android.synthetic.main.fragment_recommendations.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class RecommendationsFragment : Fragment() {

    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var viewAdapter: RecommendedArtistsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController

    private var binding: FragmentRecommendationsBinding? = null
    private val args: RecommendationsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_recommendations, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)

        setViewModel()
        setAdapter()
        observeArtists()
        observeDetail()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            RecommendationsViewModelFactory(args.country)
        ).get(RecommendationsViewModel::class.java)

        binding?.viewmodel = viewModel
        binding?.lifecycleOwner = this
    }

    private fun setAdapter() {
        viewAdapter = RecommendedArtistsAdapter(mutableListOf<MusicoveryArtist>(), viewModel::onArtistClicked, viewModel::loadImage)

        recyclerView = recommended_artists_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun observeArtists() =
        viewModel.artists.observe(
            viewLifecycleOwner,
            Observer {
                viewAdapter.updateData(viewAdapter.artists as MutableList<Any>, it)
                view?.makeLongSnackbar(this.getString(R.string.country_recommendations) + " ${args.country.toUpperCase(Locale.ROOT)}")
            }
        )

    private fun observeDetail() =
        viewModel.detail.observe(viewLifecycleOwner, EventObserver { artistName ->
            val action = RecommendationsFragmentDirections.actionRecommendationsFragmentToArtistDetailFragment(artistName)
            navController.navigate(action)
        })
}
