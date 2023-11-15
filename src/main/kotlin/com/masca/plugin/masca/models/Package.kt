package com.masca.plugin.masca.models

data class Package(
        val ID: String?,
        val Name: String,
        val Version: String,
        val License: String?,
        val Indirect: Boolean,
        val DependsOn: List<String>?,
        val FilePath: String,
)