package com.xynderous.vatole.photoviewer.presenter.photo_details

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
            fetchPhotos(id,pageNumber)
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

    private fun fetchPhotos(id: String,page:Int) {
        viewModelScope.launch {
            imageDescription(id,pageNumber).collect {
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

            }
        }
    }

    override fun onCleared() {
        savedStateHandle[AppConstants.PAGE_NUMBER_KEY] = pageNumber
        super.onCleared()
    }
}