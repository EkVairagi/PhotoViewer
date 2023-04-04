package com.xynderous.vatole.photoviewer.data.repositories

import com.xynderous.vatole.photoviewer.model.PhotoModel
import com.xynderous.vatole.photoviewer.utils.Resource

import kotlinx.coroutines.flow.Flow


interface PhotosRepository {
    suspend fun loadPhotos(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): Flow<Resource<List<PhotoModel>>>

    suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<Resource<List<PhotoModel>>>
}