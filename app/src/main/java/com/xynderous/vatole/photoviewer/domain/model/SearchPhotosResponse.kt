package com.xynderous.vatole.photoviewer.domain.model

import com.google.gson.annotations.SerializedName

data class SearchPhotosResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val photosList: List<PhotoModel>
)
