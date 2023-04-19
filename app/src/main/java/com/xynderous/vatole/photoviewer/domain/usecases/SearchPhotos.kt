package com.xynderous.vatole.photoviewer.domain.usecases

import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
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

class SearchPhotos @Inject constructor(private val repository: PhotosRepository) {
    /*operator fun invoke(query: String,
                        pageNumber: Int = 1,
                        pageSize: Int = AppConstants.QUERY_PAGE_SIZE): Flow<Resource<SearchPhotosResponse>> = flow {
        try {
            emit(Resource.Loading())
            val data = repository.searchPhotos(query,pageNumber,pageSize)
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An Unknown error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Connectivity"))
        } catch (e: Exception) {

        }
    }.flowOn(Dispatchers.IO)*/

    suspend operator fun invoke(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<Resource<SearchPhotosResponse>> {
        return repository.searchPhotos(query, pageNumber, pageSize)
    }

}