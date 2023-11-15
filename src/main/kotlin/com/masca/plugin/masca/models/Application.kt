package com.masca.plugin.masca.models

data class Application(
        val Type: String,
        val FilePath: String,
        val Libraries: List<Package>
)
