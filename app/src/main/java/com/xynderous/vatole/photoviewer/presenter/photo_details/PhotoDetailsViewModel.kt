package com.xynderous.vatole.photoviewer.presenter.photo_details

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val imageDescription: ImageDescription,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _photoDetails = MutableStateFlow(PhotoDetailsState())
    val photoDetails: StateFlow<PhotoDetailsState> = _photoDetails

    private var pageNumber: Int = 1

    fun loadPhotosById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchPhotos(pageNumber, id)
        }
    }

    init {
        savedStateHandle.get<Int>(AppConstants.PAGE_NUMBER_KEY)?.let { savedPageNumber ->
            pageNumber = savedPageNumber
        }
    }

    fun saveState(outState: Bundle) {
        outState.putInt(AppConstants.PAGE_NUMBER_KEY, pageNumber)
    }

    fun restoreState(savedInstanceState: Bundle) {
        pageNumber = savedInstanceState.getInt(AppConstants.PAGE_NUMBER_KEY)
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

    override fun onCleared() {
        savedStateHandle[AppConstants.PAGE_NUMBER_KEY] = pageNumber
        super.onCleared()
    }
}