package com.xynderous.vatole.photoviewer.data.api

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.data.model.DomainSearchPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosAPI {

    @GET("photos")
    suspend fun loadPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") numOfPhotos: Int = 20,
        @Query("order_by") orderBy: String = "popular"
    ): List<DomainPhotoModel>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") numOfPhotos: Int = 20,
    ): DomainSearchPhotosResponse


    @GET("photos/{id}")
    suspend fun imageDescription(
        @Path("id") id: String,
        @Query("page") page: Int = 1
    ): DomainPhotoModel

}