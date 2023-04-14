package com.xynderous.vatole.photoviewer.data.repositories

import com.xynderous.vatole.photoviewer.data.api.PhotosAPI
import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.toDomainPhotos
import com.xynderous.vatole.photoviewer.utils.toDomainSearchPhotos


class PhotosRepositoryImpl(private val photosApi: PhotosAPI) : PhotosRepository {

    override suspend fun loadPhotos(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): List<PhotoModel> {
        return photosApi.loadPhotos(pageNumber,pageSize,orderBy).map { it.toDomainPhotos() }
    }

    override suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): SearchPhotosResponse {
        return photosApi.searchPhotos(query,pageNumber,pageSize).toDomainSearchPhotos()
    }

    override suspend fun imageDescription(
        id: String,
        pageNumber: Int
    ): PhotoModel {
        return photosApi.imageDescription(id,pageNumber).toDomainPhotos()
    }

}
