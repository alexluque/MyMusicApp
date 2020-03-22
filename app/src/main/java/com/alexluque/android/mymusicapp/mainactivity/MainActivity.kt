package com.alexluque.android.mymusicapp.mainactivity

import ArtistData
import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.model.controllers.ConnectivityController
import com.alexluque.android.mymusicapp.mainactivity.presenters.MainActivityPresenter
import com.alexluque.android.mymusicapp.mainactivity.presenters.contracts.MainActivityContract
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.FavouriteArtistsAdapter
import com.alexluque.android.mymusicapp.mainactivity.ui.listeners.LocationRecommendationsListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract {

    private val activityView: View by lazy { findViewById<View>(android.R.id.content) }
    private val presenter: MainActivityPresenter by lazy { MainActivityPresenter() }
    private val viewAdapter: RecyclerView.Adapter<*> by lazy { FavouriteArtistsAdapter(myDataSet, this) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val myDataSet: MutableList<ArtistData> by lazy { loadData() }
    private val recommendationButton: Button by lazy { recommend_button }
    private val fusedClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val addButton: FloatingActionButton by lazy { add_button }
    private val searchArtistFragment: SearchArtistFragment by lazy { SearchArtistFragment() }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter.onCreate(this, searchArtistFragment)

        recyclerView = artists_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        setOnClickListeners()
        registerConnectivityControllerCallback()
    }

    override fun onResume() {
        super.onResume()
        ConnectivityController.view = activityView
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showRecommendations(countryName: String) = presenter.onRecommendationsClicked(this, countryName)

    override fun showDefaultRecommendations(countryName: String) = presenter.onRecommendationsClicked(this, countryName)

    private fun setOnClickListeners() {
        recommendationButton.setOnClickListener {
            Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(LocationRecommendationsListener(this, this, fusedClient)).check()
        }
        addButton.setOnClickListener {
            presenter.onClickAddButton(supportFragmentManager)
        }
    }

    private fun registerConnectivityControllerCallback() = ConnectivityController.registerCallback(this, activityView)

    private fun loadData(): MutableList<ArtistData> =
        // TODO: retrieves all artists with existing favourite songs in the DB
        mutableListOf(
            ArtistData(
                119,
                "Metallica",
                "https://www.deezer.com/artist/119",
                "https://api.deezer.com/artist/119/image",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/56x56-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/250x250-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/500x500-000000-80-0-0.jpg",
                "https://e-cdns-images.dzcdn.net/images/artist/b4719bc7a0ddb4a5be41277f37856ae6/1000x1000-000000-80-0-0.jpg",
                31,
                5573986,
                true,
                "https://api.deezer.com/artist/119/top?limit=50",
                "artist"
            ),
            ArtistData(
                1245,
                "Trivium",
                "https://www.deezer.com/artist/1245",
                "https://api.deezer.com/artist/1245/image",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/56x56-000000-80-0-0.jpg",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/250x250-000000-80-0-0.jpg",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/500x500-000000-80-0-0.jpg",
                "https://cdns-images.dzcdn.net/images/artist/45a3d4384690950e830df0ca42fabc11/1000x1000-000000-80-0-0.jpg",
                35,
                236736,
                true,
                "https://api.deezer.com/artist/1245/top?limit=50",
                "artist"
            )
        )
}