package com.xynderous.vatole.photoviewer.api

import com.xynderous.vatole.photoviewer.model.PhotoModel
import com.xynderous.vatole.photoviewer.model.SearchPhotosResponse
import retrofit2.http.GET
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


    ///https://api.unsplash.com/photos/1NCcWi24FRs?page=1&client_id=K9TTj14DjBSxfc9fU-y9rDPkOxc2IcVyTzs_yes3zIA

}