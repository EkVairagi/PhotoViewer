package com.xynderous.vatole.photoviewer.data.repositories

import com.xynderous.vatole.photoviewer.data.api.PhotosAPI
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.data.model.DomainSearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PhotosRepositoryImpl(private val photosApi: PhotosAPI) : PhotosRepository {

    override fun loadPhotos(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): Flow<Resource<List<DomainPhotoModel>>> {
        return flow {
            val photos = photosApi.loadPhotos(pageNumber, pageSize, orderBy)
            emit(Resource.Success(photos))
        }

    }

    override fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<Resource<DomainSearchPhotosResponse>> {
        return flow {
            //emit(Resource.Loading(null))
            val photos = photosApi.searchPhotos(query, pageNumber, pageSize)
            emit(Resource.Success(photos))
        }
    }

    override fun imageDescription(id: String, pageNumber: Int): Flow<Resource<DomainPhotoModel>> {
        return flow {
            //emit(Resource.Loading(null))
            val photoModel = photosApi.imageDescription(id, pageNumber)
            emit(Resource.Success(photoModel))
        }
    }


}
