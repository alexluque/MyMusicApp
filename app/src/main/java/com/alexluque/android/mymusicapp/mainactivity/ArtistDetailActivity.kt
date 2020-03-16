package com.alexluque.android.mymusicapp.mainactivity

import SongsData
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexluque.android.mymusicapp.mainactivity.presenters.ArtistDetailActivityPresenter
import com.alexluque.android.mymusicapp.mainactivity.ui.adapters.ArtistDetailAdapter
import com.alexluque.android.mymusicapp.mainactivity.ui.contracts.ArtistDetailActivityContract
import kotlinx.android.synthetic.main.activity_artist_detail.*

class ArtistDetailActivity : AppCompatActivity(), ArtistDetailActivityContract {

    private val progressBar: ProgressBar by lazy { artist_progressBar }
    private val artistImage: ImageView by lazy { artist_image }
    private val artistName: TextView by lazy { artist_name }
    private val presenter: ArtistDetailActivityPresenter by lazy { ArtistDetailActivityPresenter() }
    private val viewAdapter: RecyclerView.Adapter<*> by lazy { ArtistDetailAdapter(myDataSet) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val myDataSet: SongsData by lazy { SongsData() }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)

        presenter.onCreate(this, this)

        recyclerView = artist_detail_recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        onStartActivityReceived()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun onStartActivityReceived() = presenter.onStartActivityReceived(intent, artistName, artistImage, myDataSet, viewAdapter)
}
