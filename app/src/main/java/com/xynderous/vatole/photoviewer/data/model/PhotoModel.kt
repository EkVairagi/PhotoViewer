package com.xynderous.vatole.photoviewer.data.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoModel(
    val id: String? = "",
    val created_at: String? = "",
    val color: String? = "",
    val description: String? = "",
    val alt_description: String? = "",
    val urls: PhotoUrlsModel? = null,
    val user: UserModel? = null
) : Parcelable