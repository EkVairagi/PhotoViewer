package com.xynderous.vatole.photoviewer.domain.usecases

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ImageDescription @Inject constructor(private val repository: PhotosRepository) {
    suspend operator fun invoke(
        id: String,
        pageNumber: Int
    ): Flow<Resource<DomainPhotoModel>> {
        return repository.imageDescription(id, pageNumber)
    }
}