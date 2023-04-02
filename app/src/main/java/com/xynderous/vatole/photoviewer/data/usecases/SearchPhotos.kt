package com.xynderous.vatole.photoviewer.data.usecases

import com.xynderous.vatole.photoviewer.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.AppConstants
import javax.inject.Inject

class SearchPhotos @Inject constructor(private val repository: PhotosRepository) {

    suspend operator fun invoke(
        query:String,
        pageNumber: Int = 1,
        pageSize: Int = AppConstants.QUERY_PAGE_SIZE
    ) = repository.searchPhotos(query = query, pageNumber = pageNumber, pageSize = pageSize)

}