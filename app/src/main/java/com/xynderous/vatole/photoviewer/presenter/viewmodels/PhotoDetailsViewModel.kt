package com.xynderous.vatole.photoviewer.presenter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.domain.model.PhotoDetailsState
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val imageDescription: ImageDescription
) : ViewModel() {
    private val _photoDetails = MutableStateFlow(PhotoDetailsState())
    val photoDetails: StateFlow<PhotoDetailsState> = _photoDetails

    private var pageNumber: Int = 1

    fun loadPhotosById(id: String) {
        viewModelScope.launch {
            fetchPhotos(pageNumber, id)
        }
    }

    private fun fetchPhotos(page: Int, id: String) {
        imageDescription(page, id).onEach {
            when (it) {
                is Resource.Loading -> {
                    _photoDetails.value = PhotoDetailsState(isLoading = true)
                }
                is Resource.Error -> {
                    _photoDetails.value = PhotoDetailsState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _photoDetails.value = PhotoDetailsState(data = it.data)
                }
            }

        }.launchIn(viewModelScope)
    }

}