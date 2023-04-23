package com.xynderous.vatole.photoviewer.domain.usecases

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPopularImages @Inject constructor(private val repository: PhotosRepository) {
    operator fun invoke(
        pageNumber: Int,
        pageSize: Int,
        orderBy: String
    ): Flow<Resource<List<DomainPhotoModel>>> {
        return repository.loadPhotos(pageNumber, pageSize, orderBy)
    }
}

