package com.xynderous.vatole.photoviewer.presenter.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoState
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
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
            fetchPopularImages(page).collect { dataState->
                when(dataState){
                    is Resource.Loading -> {
                        val currentState = _photoDetails.value
                        val updatedState = currentState.copy(
                            isLoading = true,
                        )
                        _photoDetails.emit(updatedState)
                    }
                    is Resource.Success -> {
                        val currentState = _photoDetails.value

                        val a = currentState.data ?: mutableListOf()
                        val b = dataState.data ?: mutableListOf()
                        val c = a+b

                        val updatedState = currentState.copy(
                            data = c
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

    private fun searchPhotos(page: Int, searchQuery: String) {
        searchPhotosCases(searchQuery,page).onEach {
            when (it) {
                is Resource.Loading -> {
                    _photoDetails.emit(PhotoState(isLoading = true))
                }
                is Resource.Success -> {
                    _photoDetails.emit(PhotoState(data = it.data?.photosList))
                }
                is Resource.Error -> {

                    _photoDetails.emit(PhotoState(error = it.message ?: ""))
                }
            }
        }.launchIn(viewModelScope)
    }
}