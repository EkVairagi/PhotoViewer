package com.xynderous.vatole.photoviewer.presenter.photo_dashboard

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel

sealed class PhotoState {
    object Loading : PhotoState()
    data class Data(val photos: List<DomainPhotoModel>) : PhotoState()

    //data class Data<T>(val photos: T) : PhotoState()


    data class Error(val message: String) : PhotoState()
}


