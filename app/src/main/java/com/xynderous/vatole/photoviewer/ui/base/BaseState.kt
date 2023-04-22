package com.xynderous.vatole.photoviewer.ui.base

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel


sealed class BaseState {
    object Loading : BaseState()
    data class Data<T>(val photos: T) : BaseState()
    data class Error(val message: String) : BaseState()
}
