package com.xynderous.vatole.photoviewer.ui.photo_details

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.ui.base.BaseState
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
    private val _photoDetails = MutableStateFlow<BaseState>(BaseState.Loading)
    val photoDetails: StateFlow<BaseState> = _photoDetails

    var pageNumber: Int = 1

    fun loadPhotosById(id: String) {
        fetchPhotos(id, pageNumber)
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

    private fun fetchPhotos(id: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            imageDescription(id, page).collect { dataState ->
                when (dataState) {
                    is Resource.Error -> {
                        _photoDetails.value = BaseState.Error(dataState.message ?: "")
                    }
                    is Resource.Loading -> {
                        _photoDetails.value = BaseState.Loading
                    }
                    is Resource.Success -> {
                        _photoDetails.value = BaseState.Data(dataState.data)
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