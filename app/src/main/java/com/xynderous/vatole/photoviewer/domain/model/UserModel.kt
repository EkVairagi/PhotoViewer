package com.xynderous.vatole.photoviewer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String? = "",
    val username: String? = "",
    val location: String? = "",
    val name: String? = ""
) : Parcelable

