package com.xynderous.vatole.photoviewer.domain.repositories

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.data.model.DomainSearchPhotosResponse
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.flow.Flow


interface PhotosRepository {

        fun loadPhotos(
            pageNumber: Int,
            pageSize: Int,
            orderBy: String
        ): Flow<Resource<List<DomainPhotoModel>>>

        fun searchPhotos(
            query: String,
            pageNumber: Int,
            pageSize: Int
        ): Flow<Resource<DomainSearchPhotosResponse>>

        fun imageDescription(
            id: String,
            pageNumber: Int
        ): Flow<Resource<DomainPhotoModel>>



}