package com.alexluque.android.mymusicapp.mainactivity.model.network.entities.geocoding

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)