package com.alexluque.android.mymusicapp.mainactivity.model.network.entities.deezer

data class SongsData (
	val data : List<SongData> = ArrayList(),
	val total : Int = 0,
	val next : String = String()
)