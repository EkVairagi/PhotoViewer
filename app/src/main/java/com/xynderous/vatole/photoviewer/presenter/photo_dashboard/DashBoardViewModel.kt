package com.xynderous.vatole.photoviewer.presenter.photo_dashboard

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.utils.AppConstants
import com.xynderous.vatole.photoviewer.utils.AppConstants.Companion.PAGE_NUMBER_KEY
import com.xynderous.vatole.photoviewer.utils.AppConstants.Companion.SEARCH_QUERY_KEY
import com.xynderous.vatole.photoviewer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _photoDetails = MutableStateFlow(PhotoState())
    val photoDetails = _photoDetails.asStateFlow()
    var pageNumber: Int = 1
    var searchQuery: String = ""

    init {
        savedStateHandle.get<Int>(PAGE_NUMBER_KEY)?.let { savedPageNumber ->
            pageNumber = savedPageNumber
        }
        savedStateHandle.get<String>(SEARCH_QUERY_KEY)?.let { savedSearchQuery ->
            searchQuery = savedSearchQuery
        }
    }

    fun saveState(outState: Bundle) {
        outState.putInt(PAGE_NUMBER_KEY, pageNumber)
        outState.putString(SEARCH_QUERY_KEY, searchQuery)
    }

    fun restoreState(savedInstanceState: Bundle) {
        pageNumber = savedInstanceState.getInt(PAGE_NUMBER_KEY)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY, "")
    }


    fun fetchPhotosAPI() {
        if (_photoDetails.value.data.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchPhotos(pageNumber)
            }
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
            fetchPopularImages(page,AppConstants.QUERY_PAGE_SIZE,"popular").collect { dataState ->
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
            searchPhotosCases(searchQuery, page,AppConstants.QUERY_PAGE_SIZE).collect { dataState ->
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

    override fun onCleared() {
        savedStateHandle[PAGE_NUMBER_KEY] = pageNumber
        savedStateHandle[SEARCH_QUERY_KEY] = searchQuery
        super.onCleared()
    }

}