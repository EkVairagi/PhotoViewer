package com.xynderous.vatole.photoviewer.presenter.photo_dashboard

import android.os.Bundle
import android.util.Log
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
import com.xynderous.vatole.photoviewer.utils.toDomainPhotos
import com.xynderous.vatole.photoviewer.utils.toDomainSearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _photoDetails = MutableStateFlow<PhotoState>(PhotoState.Loading)
    val photoDetails: StateFlow<PhotoState> = _photoDetails
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
        if (_photoDetails.value == PhotoState.Loading) {
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
            val currentData = (_photoDetails.value as? PhotoState.Data)?.photos ?: emptyList()
            _photoDetails.value = PhotoState.Loading
            fetchPopularImages(page, AppConstants.QUERY_PAGE_SIZE, "popular").collect { dataState ->
                when (dataState) {
                    is Resource.Loading -> {
                        // Handle loading state
                    }
                    is Resource.Success -> {
                        val newData = dataState.data ?: emptyList()
                        _photoDetails.value = PhotoState.Data(currentData + newData)
                    }
                    is Resource.Error -> {
                        _photoDetails.value = PhotoState.Error(dataState.message.orEmpty())
                    }
                }
            }
        }
    }



    fun searchPhotos(page: Int, query: String) {
        viewModelScope.launch {
            val currentData = if (page > 1) (_photoDetails.value as? PhotoState.Data)?.photos.orEmpty() else emptyList()
            _photoDetails.value = PhotoState.Loading
            searchPhotosCases(query, page, AppConstants.QUERY_PAGE_SIZE).collect { dataState ->
                when (dataState) {
                    is Resource.Loading -> {
                        // Handle loading state
                    }
                    is Resource.Success -> {
                        val newData = dataState.data?.photosList.orEmpty()
                        val updatedList = currentData + newData
                        _photoDetails.value = PhotoState.Data(updatedList)
                    }
                    is Resource.Error -> {
                        _photoDetails.value = PhotoState.Error(dataState.message.orEmpty())
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