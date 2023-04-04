package com.xynderous.vatole.photoviewer.di

import android.app.Application
import com.xynderous.vatole.photoviewer.api.PhotosAPI
import com.xynderous.vatole.photoviewer.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.repositories.PhotosRepositoryImpl
import com.xynderous.vatole.photoviewer.utils.AppEnum
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideStringUtils(app:Application):AppEnum {
        return AppEnum(app)
    }

    @Singleton
    @Provides
    fun provideRepository(appEnum: AppEnum, photosAPI: PhotosAPI): PhotosRepository {
        return PhotosRepositoryImpl(appEnum,photosAPI)
    }

}