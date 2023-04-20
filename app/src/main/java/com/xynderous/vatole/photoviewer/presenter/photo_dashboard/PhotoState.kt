package com.xynderous.vatole.photoviewer.presenter.photo_dashboard

import com.xynderous.vatole.photoviewer.data.model.PhotoModel

sealed class PhotoState {
    object Loading : PhotoState()
    data class Data(val photos: List<PhotoModel>) : PhotoState()
    data class Error(val message: String) : PhotoState()
}


