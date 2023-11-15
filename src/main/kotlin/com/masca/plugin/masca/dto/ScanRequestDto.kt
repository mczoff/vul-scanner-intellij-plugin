package com.masca.plugin.masca.dto

import com.google.gson.annotations.SerializedName

data class ScanRequestDto(
        @SerializedName("apiToken") val ApiToken: String,
        @SerializedName("ide") val IDE: String,
        @SerializedName("projectName") val ProjectName: String,
        @SerializedName("packages") val Packages: List<PackageRequestDto>
)