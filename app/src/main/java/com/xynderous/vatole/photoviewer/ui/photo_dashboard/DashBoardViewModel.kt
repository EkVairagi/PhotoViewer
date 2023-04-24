package com.xynderous.vatole.photoviewer.ui.photo_dashboard

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.ui.base.BaseState
import com.xynderous.vatole.photoviewer.utils.AppConstants
import com.xynderous.vatole.photoviewer.utils.AppConstants.Companion.PAGE_NUMBER_KEY
import com.xynderous.vatole.photoviewer.utils.AppConstants.Companion.SEARCH_QUERY_KEY
import com.xynderous.vatole.photoviewer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _photoDetails = MutableStateFlow<BaseState>(BaseState.Loading)
    val photoDetails: StateFlow<BaseState> = _photoDetails


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
        fetchPhotos(pageNumber)
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
        viewModelScope.launch(Dispatchers.IO) {

            val currentData =
                (_photoDetails.value as? BaseState.Data<List<DomainPhotoModel>>)?.photos
                    ?: emptyList()
            _photoDetails.value = BaseState.Loading
            fetchPopularImages(page, AppConstants.QUERY_PAGE_SIZE, "popular").collect { dataState ->
                when (dataState) {
                    is Resource.Success -> {
                        val newData = dataState.data
                        _photoDetails.value = BaseState.Data(currentData + newData)
                    }
                    is Resource.Loading -> {
                        _photoDetails.value = BaseState.Loading
                    }
                    is Resource.Error -> {
                        _photoDetails.value = BaseState.Error(dataState.message ?: "")
                    }
                }
            }
        }
    }

    fun searchPhotos(page: Int, query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentData =
                if (page > 1) (_photoDetails.value as? BaseState.Data<List<DomainPhotoModel>>)?.photos else emptyList()
            _photoDetails.value = BaseState.Loading
            searchPhotosCases(query, page, AppConstants.QUERY_PAGE_SIZE).collect { dataState ->
                when (dataState) {
                    is Resource.Success -> {
                        val newData = dataState.data.results
                        val updatedList = (currentData ?: listOf()) + (newData ?: listOf())
                        _photoDetails.value = BaseState.Data(updatedList)
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Error -> {
                        _photoDetails.value = BaseState.Error(dataState.message.orEmpty())
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