package com.alexluque.android.mymusicapp.mainactivity

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.SearchArtistFragment.Companion.FRAGMENT_NAME
import com.alexluque.android.mymusicapp.mainactivity.controller.EventObserver
import com.alexluque.android.mymusicapp.mainactivity.controller.LocationRecommendationsListener
import com.alexluque.android.mymusicapp.mainactivity.controller.extensions.updateData
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.MainViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.MainViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.databinding.FragmentMainBinding
import com.alexluque.android.mymusicapp.mainactivity.model.database.entities.Artist
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.FavouriteArtistsAdapter
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import kotlinx.android.synthetic.main.fragment_main.*

@Suppress("UNCHECKED_CAST")
class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FavouriteArtistsAdapter
    private lateinit var navController: NavController

    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_main, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)

        setViewModel()
        setAdapter()
        setOnClickListeners()

        viewModel.artists.observe(viewLifecycleOwner, Observer(::observeContent))
        observeNavigation()
        observeRecommendation()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadArtists()
    }

    private fun setViewModel() {
        activity?.let {
            viewModel = ViewModelProvider(
                this,
                MainViewModelFactory(it.application)
            ).get(MainViewModel::class.java)
        }

        binding?.viewmodel = viewModel
        binding?.lifecycleOwner = this
    }

    private fun setAdapter() {
        viewAdapter = FavouriteArtistsAdapter(mutableListOf<Artist>(), viewModel::onArtistClicked)

        recyclerView = artists_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun setOnClickListeners() {
        recommend_button.setOnClickListener { addLocationPermission() }
        recommend_floating_btn.setOnClickListener { addLocationPermission() }
        search_button.setOnClickListener { activity?.let { SearchArtistFragment().show(it.supportFragmentManager, FRAGMENT_NAME) } }
    }

    private fun observeContent(artists: List<Artist>) =
        viewAdapter.updateData(viewAdapter.artists as MutableList<Any>, artists)

    private fun addLocationPermission() =
        activity?.let {
            Dexter.withActivity(it)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(
                    LocationRecommendationsListener(
                        getString(R.string.google_maps_key),
                        viewModel::onRecommendClicked,
                        LocationServices.getFusedLocationProviderClient(it)
                    )
                ).check()
        }

    private fun observeRecommendation() =
        viewModel.country.observe(viewLifecycleOwner, EventObserver { country ->
            val action = MainFragmentDirections.actionMainFragmentToRecommendationsFragment(country)
            navController.navigate(action)
        })

    private fun observeNavigation() =
        viewModel.artistName.observe(viewLifecycleOwner, EventObserver { artistName ->
            val action = MainFragmentDirections.actionMainFragmentToArtistDetailFragment(artistName)
            navController.navigate(action)
        })

}