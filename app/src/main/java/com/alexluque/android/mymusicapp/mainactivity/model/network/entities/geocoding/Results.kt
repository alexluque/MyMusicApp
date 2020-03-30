package com.alexluque.android.mymusicapp.mainactivity.model.network.entities.geocoding

data class Results(
    val address_components: List<Address_components>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val types: List<String>
)