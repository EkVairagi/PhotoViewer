package com.xynderous.vatole.photoviewer.utils

import com.xynderous.vatole.photoviewer.data.model.*
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoUrlsModel
import com.xynderous.vatole.photoviewer.domain.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.model.UserModel

fun PhotoModel.toDomainPhotos(): DomainPhotoModel {
    return DomainPhotoModel(
        id = this.id ?: "",
        created_at = this.created_at ?: "",
        color = this.color ?: "",
        description = this.description ?: "",
        alt_description = this.alt_description ?: "",
        urls = this.urls?.toDomainPhotoUrls(),
        user = this.user?.toDomainUser()
    )
}


fun PhotoUrlsModel.toDomainPhotoUrls(): DomainPhotoUrlsModel {
    return DomainPhotoUrlsModel(
        raw = this.raw ?: "",
        full = this.full ?: "",
        regular = this.regular ?: "",
        small = this.small ?: "",
        thumb = this.thumb ?: ""
    )
}


fun UserModel.toDomainUser(): DomainUserModel {
    return DomainUserModel(
        id = this.id  ?: "",
        username = this.username ?: "",
        location = this.location ?: "",
        name = this.name ?: ""
    )
}

fun SearchPhotosResponse.toDomainSearchPhoto(): DomainSearchPhotosResponse {
    return DomainSearchPhotosResponse(
        total = this.total ,
        total_pages = this.totalPages,
        results = this.photosList.map { it.toDomainPhotos() }
    )
}







