package com.xynderous.vatole.photoviewer.domain.model

import com.xynderous.vatole.photoviewer.data.model.PhotoModel

data class PhotoDetailsState(
    val isLoading: Boolean = false,
    val data: PhotoModel? = null,
    val error: String = ""
)