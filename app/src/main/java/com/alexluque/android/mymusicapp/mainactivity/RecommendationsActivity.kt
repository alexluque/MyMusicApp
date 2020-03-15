package com.alexluque.android.mymusicapp.mainactivity

import MusicoveryArtist
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.extensions.makeLongSnackbar
import com.alexluque.android.mymusicapp.mainactivity.presenters.RecommendationsActivityPresenter
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.RecommendedArtistsAdapter
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.RecommendationsActivityContract
import kotlinx.android.synthetic.main.activity_recommendations.*

class RecommendationsActivity : AppCompatActivity(), RecommendationsActivityContract {

    private val recommendationsView: View by lazy { findViewById<View>(android.R.id.content) }
    private val presenter: RecommendationsActivityPresenter by lazy { RecommendationsActivityPresenter() }
    private val viewAdapter: RecyclerView.Adapter<*> by lazy { RecommendedArtistsAdapter(myDataSet) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val myDataSet: MutableList<MusicoveryArtist> by lazy { mutableListOf<MusicoveryArtist>() }
    private val countryName: String by lazy { intent.getStringExtra(EXTRA_MESSAGE) }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        presenter.onCreate(this)

        recyclerView = recommended_artists_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        showRecommendations()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showRecommendations() {
        presenter.showRecommendations(viewAdapter, myDataSet, countryName)
        recommendationsView.makeLongSnackbar(getString(R.string.country_recommendations) + " ${countryName.toUpperCase()}")
    }

}
