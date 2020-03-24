package com.alexluque.android.mymusicapp.mainactivity

import MusicoveryArtist
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModel.UiModel
import com.alexluque.android.mymusicapp.mainactivity.controller.viewmodels.RecommendationsViewModelFactory
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.RecommendedArtistsAdapter
import kotlinx.android.synthetic.main.activity_recommendations.*

class RecommendationsActivity : AppCompatActivity() {

    private val activityView: View by lazy { findViewById<View>(android.R.id.content) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val myDataSet: MutableList<MusicoveryArtist> by lazy { mutableListOf<MusicoveryArtist>() }
    private val countryName: String by lazy { intent.getStringExtra(EXTRA_MESSAGE) }
    private val progress: ProgressBar by lazy { recommendations_progressBar }

    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var recommendationsAdapter: RecommendedArtistsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        recommendationsAdapter = RecommendedArtistsAdapter(myDataSet, this)
        viewModel = ViewModelProvider(this, RecommendationsViewModelFactory(countryName, recommendationsAdapter, myDataSet))
            .get(RecommendationsViewModel::class.java)
        recyclerView = recommended_artists_recycler_view.apply {
            layoutManager = viewManager
            adapter = recommendationsAdapter
        }

        viewModel.model.observe(this, Observer(::updateUi))
    }

    fun updateUi(model: UiModel) {
        progress.visibility = if (model == UiModel.Loading) View.VISIBLE else View.GONE

//        when (model) {
//            is UiModel.Content -> recommendationsAdapter.myDataSet = model.recommendations
//            is UiModel.Navigation -> this.myStartActivity(RecommendationsActivity::class.java, listOf(EXTRA_MESSAGE to countryName))
//        }
    }
}
