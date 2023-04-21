package com.xynderous.vatole.photoviewer.data.model

data class DomainPhotoModel(
    val id: String?,
    val created_at: String?,
    val color: String?,
    val description: String?,
    val alt_description: String?,
    val urls: DomainPhotoUrlsModel?,
    val user: DomainUserModel?
)