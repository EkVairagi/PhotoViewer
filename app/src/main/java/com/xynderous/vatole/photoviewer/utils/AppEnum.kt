package com.xynderous.vatole.photoviewer.utils

import android.app.Application
import com.xynderous.vatole.photoviewer.R

class AppEnum(val context: Application) {
    fun noNetworkErrorMessage() = context.getString(R.string.no_network_connection)
    fun somethingWentWrong() = context.getString(R.string.something_went_wrong)
}