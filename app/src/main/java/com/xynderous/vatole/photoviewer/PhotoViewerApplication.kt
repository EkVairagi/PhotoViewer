package com.xynderous.vatole.photoviewer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PhotoViewerApplication:Application()
{
    override fun onCreate() {
        super.onCreate()
    }
}