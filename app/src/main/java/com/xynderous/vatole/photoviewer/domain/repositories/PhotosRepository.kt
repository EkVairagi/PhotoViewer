package com.xynderous.vatole.photoviewer.domain.repositories

import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.data.model.PhotoModel

interface PhotosRepository {

    suspend fun loadPhotos(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): List<PhotoModel>

    suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): SearchPhotosResponse

    suspend fun imageDescription(
        id: String,
        pageNumber: Int
    ): PhotoModel

}