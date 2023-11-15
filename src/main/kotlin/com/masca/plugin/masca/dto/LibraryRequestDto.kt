package com.masca.plugin.masca.dto

import com.google.gson.annotations.SerializedName

data class LibraryRequestDto(
        @SerializedName("id") val ID: String?,
        @SerializedName("name") val Name: String,
        @SerializedName("version") val Version: String,
        @SerializedName("license") val License: String?,
        @SerializedName("indirect") val Indirect: Boolean,
        @SerializedName("dependsOn") val DependsOn: List<String>?,
)