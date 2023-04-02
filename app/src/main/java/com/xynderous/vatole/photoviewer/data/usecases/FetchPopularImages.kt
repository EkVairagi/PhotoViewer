package com.xynderous.vatole.photoviewer.data.usecases

import com.xynderous.vatole.photoviewer.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.AppConstants
import javax.inject.Inject

class FetchPopularImages @Inject constructor(private val repository: PhotosRepository) {

    suspend operator fun invoke(
        pageNumber: Int = 1,
        pageSize: Int = AppConstants.QUERY_PAGE_SIZE,
        orderBy: String = "popular"
    ) = repository.loadPhotos(pageNumber = pageNumber, pageSize = pageSize, orderBy = orderBy)

}

