package com.example.android.mymusicapp.mainactivity

import ArtistData
import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymusicapp.mainactivity.adapters.FavouriteArtistsAdapter
import com.example.android.mymusicapp.mainactivity.contracts.MainActivityContract
import com.example.android.mymusicapp.mainactivity.permission.listeners.LocationRecommendationsListener
import com.example.android.mymusicapp.mainactivity.presenters.MainActivityPresenter
import com.karumi.dexter.Dexter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainActivityContract {

    private val presenter: MainActivityPresenter by lazy { MainActivityPresenter() }
    private val viewAdapter: RecyclerView.Adapter<*> by lazy { FavouriteArtistsAdapter(myDataSet) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val myDataSet: MutableList<ArtistData> by lazy { loadData() }
    private val recommendationButton: Button by lazy { recommend_button }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView = artists_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        recommendationButton.setOnClickListener {
            Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(LocationRecommendationsListener(this, this)).check()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showRandomRecommendations() = presenter.startRecommendationsActivity(this)

    // TODO: retrieves all artists with existing favourite songs within users database
    private fun loadData(): MutableList<ArtistData> =
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