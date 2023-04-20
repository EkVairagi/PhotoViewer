package com.xynderous.vatole.photoviewer.domain.repositories

import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.flow.Flow


interface PhotosRepository {

        fun loadPhotos(
            pageNumber: Int,
            pageSize: Int,
            orderBy: String
        ): Flow<Resource<List<PhotoModel>>>

        fun searchPhotos(
            query: String,
            pageNumber: Int,
            pageSize: Int
        ): Flow<Resource<SearchPhotosResponse>>

        fun imageDescription(
            id: String,
            pageNumber: Int
        ): Flow<Resource<PhotoModel>>



}