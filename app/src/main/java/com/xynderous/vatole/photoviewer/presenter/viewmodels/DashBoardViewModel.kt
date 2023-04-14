package com.xynderous.vatole.photoviewer.presenter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.domain.model.PhotoState
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos
) : ViewModel() {
   // private var _photoDetails = MutableSharedFlow<PhotoState>(replay = 1)
    private val _photoDetails = MutableSharedFlow<PhotoState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val photoDetails: SharedFlow<PhotoState> = _photoDetails




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

                        //stateflow
                        //_photoDetails.value = PhotoState(isLoading = true)

                        //sharedflow
                        _photoDetails.emit(PhotoState(isLoading = true))

                    }
                    is Resource.Success -> {


                        /*if (page==1){
                            _photoDetails.emit(PhotoState(data = dataState.data))
                        }else{
                            val a = _photoDetails.replayCache.firstOrNull()?.data ?: listOf()
                            val b = dataState.data?: listOf()
                            val c = a.plus(b)
                            _photoDetails.emit(PhotoState(data = c))
                        }
*/
                        _photoDetails.tryEmit(PhotoState(data = dataState.data))

                       // _photoDetails.tryEmit(PhotoState().copy(data = dataState.data))

/*

                        val b = dataState.data?: listOf()
                        //val a = dataState.data?: listOf()
                        val a = _photoDetails.replayCache.firstOrNull()?.data ?: listOf()

                        //val a = _photoDetails.value.data?: listOf()


                        val c = b.plus(a)


                        Log.e("A_SIZE_DATA",a.size.toString())
                        Log.e("B_SIZE_DATA",b.size.toString())
                        Log.e("C_SIZE_DATA",c.size.toString())

                        //stateflow
                        //_photoDetails.value = _photoDetails.value.copy(data = c)
                        //_photoDetails.value = (PhotoState(data = dataState.data))

                        _photoDetails.emit(PhotoState(data = c))

*/

                        //_photoDetails.value = PhotoState(data = dataState.data ?: emptyList())

                        //sharedflow
                        //_photoDetails.emit(PhotoState(data = dataState.data))
                        _photoDetails.emit(PhotoState(data = dataState.data))

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
                    _photoDetails.emit(PhotoState(isLoading = true))
                }
                is Resource.Success -> {
                    _photoDetails.emit(PhotoState(data = it.data?.photosList))
                   // _photoDetails.value = PhotoState(data = it.data?.photosList)
                }
                is Resource.Error -> {
                   // _photoDetails.value = PhotoState(error = it.message ?: "")

                    _photoDetails.emit(PhotoState(error = it.message ?: ""))
                }
            }
        }.launchIn(viewModelScope)
    }
}