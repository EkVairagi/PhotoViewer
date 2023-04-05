package com.xynderous.vatole.photoviewer.presenter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val imageDescription: ImageDescription
) : ViewModel() {

    private var photoModelByAPI = MutableStateFlow<PhotoModel?>(null)
    var photoModelLiveDataByAPI: StateFlow<PhotoModel?> = photoModelByAPI

    private var pageNumber: Int = 1

    fun loadPhotosById(id: String) {
        fetchPhotos(pageNumber, id)
    }

    private fun fetchPhotos(page: Int, id: String) {
        viewModelScope.launch {
            imageDescription(page, id).collect { dataState ->
                when (dataState) {
                    is Resource.Success -> {
                        photoModelByAPI.emit(dataState.data)
                    }
                    is Resource.Error -> {
                    }

                }

            }
        }
    }

}