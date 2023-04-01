package com.xynderous.vatole.photoviewer.response

import com.xynderous.vatole.photoviewer.MainCoroutine
import com.xynderous.vatole.photoviewer.api.ApiAbstract
import com.xynderous.vatole.photoviewer.api.ApiResponse
import com.xynderous.vatole.photoviewer.api.PhotosAPI
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


class APIServiceTest : ApiAbstract<PhotosAPI>() {

    private lateinit var apiService: PhotosAPI

    @get:Rule
    var coroutineRule = MainCoroutine()

    @Before
    fun setUp() {
        apiService = createService(PhotosAPI::class.java)
    }

    @After
    fun tearDown() {
    }

    @Throws(IOException::class)
    @Test
    fun `test loadPhotos() returns list of Photos`() = runBlocking {
        // Given
        enqueueResponse("/photos_list_response.json")

        // Invoke
        val response = apiService.loadPhotos(1, 10, "")
        val responseBody = requireNotNull((response as ApiResponse.ApiSuccessResponse).data)
        mockWebServer.takeRequest()

        // Then
        MatcherAssert.assertThat(responseBody[0].id, CoreMatchers.`is`("LBI7cgq3pbM"))
        MatcherAssert.assertThat(responseBody[0].color, CoreMatchers.`is`("#60544D"))
        MatcherAssert.assertThat(responseBody[0].urls?.thumb, CoreMatchers.`is`("https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=200&fit=max"))
        MatcherAssert.assertThat(responseBody[0].user?.id, CoreMatchers.`is`("pXhwzz1JtQU"))
        MatcherAssert.assertThat(responseBody[0].user?.username, CoreMatchers.`is`("poorkane"))
    }

    @Throws(IOException::class)
    @Test
    fun `test searchPhotos() returns list of Photos`() = runBlocking {
        // Given
        enqueueResponse("/search_photos_response.json")

        // Invoke
        val response = apiService.searchPhotos("", 1, 10)
        val responseBody = requireNotNull((response as ApiResponse.ApiSuccessResponse).data)
        mockWebServer.takeRequest()

        // Then
        MatcherAssert.assertThat(responseBody.total, CoreMatchers.`is`(133))
        MatcherAssert.assertThat(responseBody.totalPages, CoreMatchers.`is`(7))
        MatcherAssert.assertThat(responseBody.photosList.size, CoreMatchers.`is`(1))

        MatcherAssert.assertThat(responseBody.photosList[0].id, CoreMatchers.`is`("eOLpJytrbsQ"))
        MatcherAssert.assertThat(responseBody.photosList[0].color, CoreMatchers.`is`("#A7A2A1"))
        MatcherAssert.assertThat(responseBody.photosList[0].urls?.thumb, CoreMatchers.`is`("https://images.unsplash.com/photo-1416339306562-f3d12fefd36f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=8aae34cf35df31a592f0bef16e6342ef"))
        MatcherAssert.assertThat(responseBody.photosList[0].user?.id, CoreMatchers.`is`("Ul0QVz12Goo"))
        MatcherAssert.assertThat(responseBody.photosList[0].user?.username, CoreMatchers.`is`("ugmonk"))
    }
}