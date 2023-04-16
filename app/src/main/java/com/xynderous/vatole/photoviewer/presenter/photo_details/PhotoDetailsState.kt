package com.xynderous.vatole.photoviewer.presenter.photo_details

import com.xynderous.vatole.photoviewer.data.model.PhotoModel

data class PhotoDetailsState(
    val isLoading: Boolean = false,
    val data: PhotoModel? = null,
    val error: String = ""
)