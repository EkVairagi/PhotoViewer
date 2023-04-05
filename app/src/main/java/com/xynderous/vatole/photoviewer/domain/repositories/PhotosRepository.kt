package com.xynderous.vatole.photoviewer.domain.repositories

import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
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

    suspend fun imageDescription(
        id: String,
        pageNumber: Int
    ): Flow<Resource<PhotoModel>>
}