package com.xynderous.vatole.photoviewer.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xynderous.vatole.photoviewer.data.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.data.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.model.PhotoModel
import com.xynderous.vatole.photoviewer.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val fetchPopularImages: FetchPopularImages,
    private val searchPhotosCases: SearchPhotos
) : ViewModel() {

    private var uiState = MutableLiveData<DashboardState>()
    var uiStateLiveData: LiveData<DashboardState> = uiState

    private var photosList = MutableLiveData<List<PhotoModel>>()
    var photosLiveData: LiveData<List<PhotoModel>> = photosList

    private var pageNumber: Int = 1
    private var searchQuery: String = ""

    init {
        fetchPhotos(pageNumber)
    }

    fun loadMorePhotos(){
        pageNumber++
        if (searchQuery == "")
            fetchPhotos(pageNumber)
        else
            searchPhotos(pageNumber,searchQuery)
    }

    fun retry(){
        if (searchQuery=="")
            fetchPhotos(pageNumber)
        else
            searchPhotos(pageNumber,searchQuery)
    }

    fun searchPhotos(query:String){
        searchQuery = query
        pageNumber = 1
        searchPhotos(pageNumber,query)

    }

    fun fetchPhotos(page:Int){

        uiState.postValue(if (pageNumber==1) LoadingState else LoadingNextPageState)
        viewModelScope.launch {
            fetchPopularImages(page).collect() { dataState->
                when(dataState){

                    is Resource.Success -> {

                        if (page==1){
                            uiState.postValue(ContentState)
                            photosList.postValue(dataState.data!!)
                        }else{
                            uiState.postValue(ContentNextPageState)
                            var currentList = arrayListOf<PhotoModel>()
                            photosList.value?.let {
                                currentList.addAll(it)
                            }
                            currentList.addAll(dataState.data)
                            photosList.postValue(currentList)
                        }
                    }

                    is Resource.Error -> {
                        if (page==1)
                            uiState.postValue(ErrorState(dataState.message))
                        else
                            uiState.postValue(ErrorNextPageState(dataState.message))
                    }

                }

            }
        }

    }

    private fun searchPhotos(page: Int, searchQuery:String){

        uiState.postValue(if (page==1) LoadingState else LoadingNextPageState)
        viewModelScope.launch {
            searchPhotosCases(searchQuery,page).collect {dataState ->

                when(dataState){

                    is Resource.Success -> {
                        if (page==1){
                            uiState.postValue(ContentState)
                            photosList.postValue(dataState.data!!)

                        }else{

                            uiState.postValue(ContentNextPageState)
                            var currentList = arrayListOf<PhotoModel>()
                            photosList.value?.let {
                                currentList.addAll(it)
                            }
                            currentList.addAll(dataState.data)
                            photosList.postValue(currentList)
                        }
                    }

                    is Resource.Error -> {
                        if (page==1)
                            uiState.postValue(ErrorState(dataState.message))
                        else
                            uiState.postValue(ErrorNextPageState(dataState.message))
                    }

                }

            }
        }
    }

}