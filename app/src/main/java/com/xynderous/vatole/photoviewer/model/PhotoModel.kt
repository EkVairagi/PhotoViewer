package com.xynderous.vatole.photoviewer.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoModel(
     val id: String? = "",
     val created_at: String? = "",
     val color: String? = "",
     val description: String? = "",
     val urls: PhotoUrlsModel? = null,
     val user: UserModel? = null
):Parcelable
