package com.xynderous.vatole.photoviewer.domain.usecases

import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.AppConstants
import javax.inject.Inject


class ImageDescription @Inject constructor(private val repository: PhotosRepository) {
    suspend operator fun invoke(
        pageSize: Int = AppConstants.QUERY_PAGE_SIZE, id: String
    ) = repository.imageDescription(id, pageSize)
}