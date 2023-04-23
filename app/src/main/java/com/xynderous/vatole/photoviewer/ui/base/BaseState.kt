package com.xynderous.vatole.photoviewer.ui.base

sealed class BaseState {
    object Loading : BaseState()
    data class Data<T>(val photos: T) : BaseState()
    data class Error(val message: String) : BaseState()
}
