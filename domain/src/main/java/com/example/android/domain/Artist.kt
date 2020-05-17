package com.example.android.domain

data class Artist (
    val mbid : String,
    val name : String,
    var genres : Any? = String(),
    var country : Any? = String(),
    var region: Any? = null
)