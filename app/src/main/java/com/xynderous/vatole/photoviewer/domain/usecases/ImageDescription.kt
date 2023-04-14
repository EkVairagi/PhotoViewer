package com.xynderous.vatole.photoviewer.domain.usecases

import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.AppConstants
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class ImageDescription @Inject constructor(private val repository: PhotosRepository) {
    operator fun invoke(
        pageSize: Int = AppConstants.QUERY_PAGE_SIZE,
        id: String
    ): Flow<Resource<PhotoModel>> = flow {
        try {
            emit(Resource.Loading())
            val data = repository.imageDescription(id,pageSize)

            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An Unknown error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Connectivity"))
        } catch (e: Exception) {

        }
    }.flowOn(Dispatchers.IO)


}