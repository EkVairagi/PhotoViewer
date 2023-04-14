package com.xynderous.vatole.photoviewer.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoUrlsModel(
    val raw: String? = "",
    val full: String? = "",
    val regular: String? = "",
    val small: String? = "",
    val thumb: String? = ""
) : Parcelable
