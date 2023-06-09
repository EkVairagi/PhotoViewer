package com.xynderous.vatole.photoviewer.domain.usecases

import com.xynderous.vatole.photoviewer.data.model.DomainSearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPhotos @Inject constructor(private val repository: PhotosRepository) {

    operator fun invoke(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<Resource<DomainSearchPhotosResponse>> {
        return repository.searchPhotos(query, pageNumber, pageSize)
    }

}