package com.example.android.mymusicapp.mainactivity

import MusicoveryArtist
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymusicapp.mainactivity.ui.adapters.RecommendedArtistsAdapter
import com.example.android.mymusicapp.mainactivity.ui.contracts.RecommendationsActivityContract
import com.example.android.mymusicapp.mainactivity.presenters.RecommendationsActivityPresenter
import kotlinx.android.synthetic.main.activity_recommendations.*

class RecommendationsActivity : AppCompatActivity(), RecommendationsActivityContract {

    private val presenter: RecommendationsActivityPresenter by lazy { RecommendationsActivityPresenter() }
    private val viewAdapter: RecyclerView.Adapter<*> by lazy { RecommendedArtistsAdapter(myDataSet) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val myDataSet: MutableList<MusicoveryArtist> by lazy { mutableListOf<MusicoveryArtist>()}

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        recyclerView = recommended_artists_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        showRandomRecommendations()
    }

    override fun showRandomRecommendations() = presenter.showRandomRecommendations(viewAdapter, myDataSet)
}
