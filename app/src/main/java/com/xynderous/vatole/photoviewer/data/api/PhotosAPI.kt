package com.xynderous.vatole.photoviewer.data.api

import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosAPI {

    @GET("photos")
    suspend fun loadPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") numOfPhotos: Int = 10,
        @Query("order_by") orderBy: String = "popular"
    ): ApiResponse<List<PhotoModel>>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") numOfPhotos: Int = 10,
    ): ApiResponse<SearchPhotosResponse>


    @GET("photos/{id}")
    suspend fun imageDescription(
        @Path("id") id: String,
        @Query("page") page: Int = 1
    ): ApiResponse<PhotoModel>

}