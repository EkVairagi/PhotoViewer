package com.xynderous.vatole.photoviewer.domain.model

data class PhotoModel(
    val id: String?,
    val created_at: String?,
    val color: String?,
    val description: String?,
    val alt_description: String?,
    val urls: PhotoUrlsModel?,
    val user: UserModel?
)