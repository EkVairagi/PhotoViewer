package com.xynderous.vatole.photoviewer.presenter.photo_details

import com.xynderous.vatole.photoviewer.domain.model.PhotoModel

sealed class PhotoDetailsViewState {
    object Loading : PhotoDetailsViewState()
    data class Data(val photo: PhotoModel) : PhotoDetailsViewState()
    data class Error(val message: String) : PhotoDetailsViewState()
}
