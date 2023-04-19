package com.xynderous.vatole.photoviewer.utils

import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoUrlsModel
import com.xynderous.vatole.photoviewer.domain.model.UserModel

fun PhotoModel.toDomainPhotos(): PhotoModel {
    return PhotoModel(
        id = this.id ?: "",
        alt_description = this.alt_description ?: "",
        description = this.description ?: "",
        color = this.color ?: "",
        created_at = this.created_at ?: "",
        urls = this.urls,
        user = this.user

    )
}

fun PhotoUrlsModel.toDomainPhotosUrls(): PhotoUrlsModel {
    return PhotoUrlsModel(
        raw = this.raw ?: "",
        full = this.full ?: "",
        regular = this.regular ?: "",
        small = this.small ?: "",
        thumb = this.thumb ?: ""
    )
}

fun UserModel.toDomainUserModel(): UserModel {
    return UserModel(
        id = this.id ?: "",
        username = this.username ?: "",
        location = this.location ?: "",
        name = this.name ?: ""
    )
}

fun SearchPhotosResponse.toDomainSearchPhotos(): SearchPhotosResponse {
    return SearchPhotosResponse(
        total = this.total,
        totalPages = this.totalPages,
        photosList = photosList
    )
}

