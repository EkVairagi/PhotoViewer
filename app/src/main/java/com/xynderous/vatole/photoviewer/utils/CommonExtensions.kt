package com.xynderous.vatole.photoviewer.utils

import android.view.View

fun View?.makeVisibleIf(condition: Boolean) {
    when (condition) {
        true -> this?.visibility = View.VISIBLE
        else -> this?.visibility = View.GONE
    }
}
