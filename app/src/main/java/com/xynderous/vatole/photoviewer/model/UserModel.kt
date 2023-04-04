package com.xynderous.vatole.photoviewer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String? = "",
    val username: String? = "",
    val name: String? = ""
) : Parcelable
