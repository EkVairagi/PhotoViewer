package com.xynderous.vatole.photoviewer.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xynderous.vatole.photoviewer.model.PhotoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsViewModel @Inject constructor() : ViewModel() {

    private var photoModel = MutableLiveData<PhotoModel>()
    var photoModelLiveData: LiveData<PhotoModel> = photoModel

    fun initPhotoModel(photo: PhotoModel) {
        photoModel.value = photo
    }

}