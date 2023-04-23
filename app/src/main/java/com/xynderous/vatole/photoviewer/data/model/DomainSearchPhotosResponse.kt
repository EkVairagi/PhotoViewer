package com.xynderous.vatole.photoviewer.data.model

data class DomainSearchPhotosResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<DomainPhotoModel>
)