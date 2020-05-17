package com.alexluque.android.mymusicapp.mainactivity.model.network.entities.musicovery

data class Artist (
	val mbid : String,
	val name : String,
	val genres : Any?,
	val country : Any?,
	val region: Any? = null
)