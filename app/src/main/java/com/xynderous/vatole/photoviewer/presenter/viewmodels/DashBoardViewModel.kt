package com.xynderous.vatole.photoviewer.presenter.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos
) : ViewModel() {

    private var photosList = MutableStateFlow<List<PhotoModel>?>(null)
    var photosLiveData: StateFlow<List<PhotoModel>?> = photosList

    private var pageNumber: Int = 1
    private var searchQuery: String = ""

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
        viewModelScope.launch {
            fetchPopularImages(page).collect { dataState ->
                when (dataState) {

                    is Resource.Success -> {

                        if (page == 1) {
                            photosList.emit(dataState.data)
                        } else {
                            val currentList = arrayListOf<PhotoModel>()
                            photosList.value?.let {
                                currentList.addAll(it)
                            }
                            currentList.addAll(dataState.data)
                            photosList.emit(currentList)
                        }
                    }

                    is Resource.Error -> {
                        if (page == 1)
                            Log.e("Error", dataState.message)
                        else
                            Log.e("Error", dataState.message)
                    }

                }

            }
        }
    }

    private fun searchPhotos(page: Int, searchQuery: String) {
        viewModelScope.launch {
            searchPhotosCases(searchQuery, page).collect { dataState ->

                when (dataState) {

                    is Resource.Success -> {
                        if (page == 1) {
                            photosList.emit(dataState.data)

                        } else {

                            val currentList = arrayListOf<PhotoModel>()
                            photosList.value?.let {
                                currentList.addAll(it)
                            }
                            currentList.addAll(dataState.data)
                            photosList.emit(currentList)
                        }
                    }

                    is Resource.Error -> {
                        if (page == 1)
                            Log.e("Error", dataState.message)
                        else
                            Log.e("Error", dataState.message)
                    }

                }

            }
        }
    }

}