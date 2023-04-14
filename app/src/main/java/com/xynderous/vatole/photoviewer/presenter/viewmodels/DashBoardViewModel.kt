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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos
) : ViewModel() {
    private var _photoDetails = MutableSharedFlow<PhotoState>(replay = 999)
    val photoDetails: SharedFlow<PhotoState> = _photoDetails

    /*private var _photoDetails = MutableStateFlow(PhotoState())
    val photoDetails: StateFlow<PhotoState> = _photoDetails*/

    private var pageNumber: Int = 1
    private var searchQuery: String = ""

    fun fetchPhotosAPI() {
        viewModelScope.launch {
            fetchPhotos(pageNumber)
        }
    }

    init {

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

                        //stateflow
                        //_photoDetails.value = PhotoState(isLoading = true)

                        //sharedflow
                        _photoDetails.emit(PhotoState(isLoading = true))

                    }
                    is Resource.Success -> {


                        val a = _photoDetails.replayCache.firstOrNull()?.data ?: listOf()



                        //val a = _photoDetails.value.data?: listOf()

                        val b = dataState.data?: listOf()

                        val c = a.plus(b)


                        Log.e("A_SIZE_DATA",a.size.toString())
                        Log.e("B_SIZE_DATA",b.size.toString())
                        Log.e("C_SIZE_DATA",c.size.toString())

                        //stateflow
                        //_photoDetails.value = _photoDetails.value.copy(data = c)
                        //_photoDetails.value = (PhotoState(data = dataState.data))

                        _photoDetails.emit(PhotoState(data = c))


                        //sharedflow
                        //_photoDetails.emit(PhotoState(data = dataState.data))

                    }
                    is Resource.Error -> {
                        //stateflow
                        //_photoDetails.value = PhotoState(error = dataState.message ?: "")
                        //sharedflow
                        _photoDetails.emit(PhotoState(error = dataState.message ?: ""))
                    }
                }
            }
        }
    }

    private fun searchPhotos(page: Int, searchQuery: String) {
        searchPhotosCases(searchQuery,page).onEach {
            when (it) {
                is Resource.Loading -> {
                    //_photoDetails.value = PhotoState(isLoading = true)
                }
                is Resource.Success -> {
                   // _photoDetails.value = PhotoState(data = it.data?.photosList)
                }
                is Resource.Error -> {
                   // _photoDetails.value = PhotoState(error = it.message ?: "")
                }
            }
        }.launchIn(viewModelScope)
    }
}