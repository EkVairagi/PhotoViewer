package com.xynderous.vatole.photoviewer.data.repositories

import android.util.Log
import com.xynderous.vatole.photoviewer.data.api.PhotosAPI
import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import com.xynderous.vatole.photoviewer.utils.toDomainPhotos
import com.xynderous.vatole.photoviewer.utils.toDomainSearchPhotos

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PhotosRepositoryImpl(private val photosApi: PhotosAPI) : PhotosRepository {

    override fun loadPhotos(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): Flow<Resource<List<PhotoModel>>> {
        return flow {
            emit(Resource.Loading(null))
            val photos =
                photosApi.loadPhotos(pageNumber, pageSize, orderBy).map { it.toDomainPhotos() }
            emit(Resource.Success(photos))
        }

    }

    override fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<Resource<SearchPhotosResponse>> {
        return flow {
            emit(Resource.Loading(null))
            val photos = photosApi.searchPhotos(query, pageNumber, pageSize).toDomainSearchPhotos()
            emit(Resource.Success(photos))
        }
    }

    override fun imageDescription(id: String, pageNumber: Int): Flow<Resource<PhotoModel>> {
        return flow {
            emit(Resource.Loading(null))
            val photoModel = photosApi.imageDescription(id, pageNumber).toDomainPhotos()
            emit(Resource.Success(photoModel))
        }
    }


}
