package com.xynderous.vatole.photoviewer.presenter.photo_details

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel



sealed class PhotoDetailsState {
    object Loading : PhotoDetailsState()
    data class Data(val photos: DomainPhotoModel) : PhotoDetailsState()
    data class Error(val message: String) : PhotoDetailsState()
}
