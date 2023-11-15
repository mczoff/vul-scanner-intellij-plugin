package com.masca.plugin.masca.dto

import com.google.gson.annotations.SerializedName

data class PackageRequestDto(
        @SerializedName("type") val Type: String,
        @SerializedName("filePath") val FilePath: String,
        @SerializedName("libraries") val Libraries: List<LibraryRequestDto>,
)