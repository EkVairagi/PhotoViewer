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

class FetchPopularImages @Inject constructor(private val repository: PhotosRepository) {
    suspend operator fun invoke(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): Flow<Resource<List<PhotoModel>>> {
        return repository.loadPhotos(pageNumber, pageSize, orderBy)
    }
}

