package com.xynderous.vatole.photoviewer.domain.model

import com.xynderous.vatole.photoviewer.data.model.PhotoModel

data class PhotoState(
    val isLoading: Boolean = false,
    val data: List<PhotoModel>? = null,
    val error: String = ""
)