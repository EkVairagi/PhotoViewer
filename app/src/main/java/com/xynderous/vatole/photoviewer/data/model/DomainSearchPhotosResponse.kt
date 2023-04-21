package com.xynderous.vatole.photoviewer.data.model

import com.xynderous.vatole.photoviewer.domain.model.PhotoModel

data class DomainSearchPhotosResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<DomainPhotoModel>
)