package com.xynderous.vatole.photoviewer.presenter.photo_dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos
) : ViewModel() {

     private val _photoDetails = MutableStateFlow(PhotoState())
    val photoDetails = _photoDetails.asStateFlow()

    private var pageNumber: Int = 1
    private var searchQuery: String = ""


    fun fetchPhotosAPI() {
        viewModelScope.launch {
            fetchPhotos(pageNumber)
        }
    }

    fun loadMorePhotos() {
        pageNumber++
        if (searchQuery == "")
            fetchPhotos(pageNumber)
        else
            searchPhotos(pageNumber, searchQuery)
    }

    fun searchPhotos(query: String) {
        searchQuery = query
        pageNumber = 1
        searchPhotos(pageNumber, query)
    }

    fun fetchPhotos(page: Int) {
        viewModelScope.launch {
            fetchPopularImages(page).collect { dataState ->
                when (dataState) {
                    is Resource.Loading -> {
                        val currentState = _photoDetails.value
                        val updatedState = currentState.copy(
                            isLoading = true,
                        )
                        _photoDetails.emit(updatedState)
                    }
                    is Resource.Success -> {
                        val currentState = _photoDetails.value

                        val updatedState = currentState.copy(
                            data = (currentState.data ?: mutableListOf()) + (dataState.data
                                ?: mutableListOf())
                        )
                        _photoDetails.emit(updatedState)

                    }
                    is Resource.Error -> {
                        val currentState = _photoDetails.value
                        val updatedState = currentState.copy(
                            isLoading = false,
                            error = dataState.message ?: ""
                        )
                        _photoDetails.emit(updatedState)
                    }
                }
            }
        }
    }


    fun searchPhotos(page: Int, searchQuery: String) {
        if (page == 1) {
            _photoDetails.value = PhotoState(isLoading = true, data = emptyList())
        }
        viewModelScope.launch {
            searchPhotosCases(searchQuery, page).collect { dataState ->
                when (dataState) {
                    is Resource.Loading -> {
                        val currentState = _photoDetails.value
                        val updatedState = currentState.copy(isLoading = true)
                        _photoDetails.emit(updatedState)
                    }
                    is Resource.Success -> {
                        val currentState = _photoDetails.value
                        val existingData = currentState.data.orEmpty()
                        val newData = dataState.data?.photosList.orEmpty()
                        val updatedState = currentState.copy(
                            data = existingData + newData,
                            isLoading = false,
                            error = ""
                        )
                        _photoDetails.emit(updatedState)
                    }
                    is Resource.Error -> {
                        val currentState = _photoDetails.value
                        val updatedState = currentState.copy(
                            isLoading = false,
                            error = dataState.message ?: ""
                        )
                        _photoDetails.emit(updatedState)
                    }
                }
            }
        }
    }


}