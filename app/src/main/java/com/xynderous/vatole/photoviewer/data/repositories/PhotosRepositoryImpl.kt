package com.xynderous.vatole.photoviewer.data.repositories

import androidx.annotation.WorkerThread
import com.xynderous.vatole.photoviewer.api.*
import com.xynderous.vatole.photoviewer.data.api.PhotosAPI
import com.xynderous.vatole.photoviewer.model.PhotoModel
import com.xynderous.vatole.photoviewer.utils.AppEnum
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val appEnum: AppEnum,
    private val photosApi: PhotosAPI
) : PhotosRepository {

    @WorkerThread
    override suspend fun loadPhotos(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): Flow<Resource<List<PhotoModel>>> {
        return flow {
            photosApi.loadPhotos(pageNumber, pageSize, orderBy).apply {
                this.onSuccessSuspend {
                    data?.let {
                        emit(Resource.success(it))
                    }
                }
            }.onErrorSuspend {
                emit(Resource.error(message()))
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(Resource.error(appEnum.noNetworkErrorMessage()))
                } else {
                    emit(Resource.error(appEnum.somethingWentWrong()))
                }
            }
        }
    }

    override suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<Resource<List<PhotoModel>>> {
        return flow {

            photosApi.searchPhotos(query, pageNumber, pageSize).apply {
                this.onSuccessSuspend {
                    data?.let {
                        emit(Resource.success(it.photosList))
                    }
                }
            }.onErrorSuspend {
                emit(Resource.error(message()))

            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(Resource.error(appEnum.noNetworkErrorMessage()))
                } else {
                    emit(Resource.error(appEnum.somethingWentWrong()))
                }
            }
        }
    }
}