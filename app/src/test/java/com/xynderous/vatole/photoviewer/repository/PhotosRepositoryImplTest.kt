package com.xynderous.vatole.photoviewer.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xynderous.vatole.photoviewer.MainCoroutine
import com.xynderous.vatole.photoviewer.api.ApiResponse
import com.xynderous.vatole.photoviewer.api.ApiUtil.successCall
import com.xynderous.vatole.photoviewer.api.PhotosAPI
import com.xynderous.vatole.photoviewer.api.message
import com.xynderous.vatole.photoviewer.model.PhotoModel
import com.xynderous.vatole.photoviewer.repositories.PhotosRepositoryImpl
import com.xynderous.vatole.photoviewer.utils.AppEnum
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class PhotosRepositoryImplTest {

    // Subject under test
    private lateinit var repository: PhotosRepositoryImpl

    @MockK
    private lateinit var apiService: PhotosAPI

    @MockK
    private lateinit var stringUtils: AppEnum

    @get:Rule
    var coroutinesRule = MainCoroutine()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `test loadPhotos() gives list of photos`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(stringUtils, apiService)
        val givenPhotosList = MockTestUtil.createPhotos(3)
        val apiCall = successCall(givenPhotosList)

        // When
        coEvery { apiService.loadPhotos(any(), any(), any()) }
            .returns(apiCall)

        // Invoke
        val apiResponseFlow = repository.loadPhotos(1, 1, "")

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())

        val photosListDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.instanceOf(Resource.Success::class.java))

        val photosList = (photosListDataState as Resource.Success).data
        MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosList.size, CoreMatchers.`is`(givenPhotosList.size))

        coVerify(exactly = 1) { apiService.loadPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }

    @Test
    fun `test loadPhotos() gives empty list of photos`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(stringUtils, apiService)
        val givenPhotosList = MockTestUtil.createPhotos(0)
        val apiCall = successCall(givenPhotosList)

        // When
        coEvery { apiService.loadPhotos(any(), any(), any()) }
            .returns(apiCall)

        // Invoke
        val apiResponseFlow = repository.loadPhotos(1, 1, "")

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())

        val photosListDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.instanceOf(Resource.Success::class.java))

        val photosList = (photosListDataState as Resource.Success).data
        MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosList.size, CoreMatchers.`is`(givenPhotosList.size))

        coVerify(exactly = 1) { apiService.loadPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }

    @Test
    fun `test loadPhotos() throws exception`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(stringUtils, apiService)
        val givenMessage = "Test Error Message"
        val exception = Exception(givenMessage)
        val apiResponse = ApiResponse.exception<List<PhotoModel>>(exception)

        // When
        coEvery { apiService.loadPhotos(any(), any(), any()) }
            .returns(apiResponse)
        coEvery { stringUtils.somethingWentWrong() }
            .returns(givenMessage)

        // Invoke
        val apiResponseFlow = repository.loadPhotos(1, 1, "")

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseFlow.count(), CoreMatchers.`is`(1))

        val apiResponseDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.instanceOf(Resource.Error::class.java))

        val errorMessage = (apiResponseDataState as Resource.Error).message
        MatcherAssert.assertThat(errorMessage, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(errorMessage, CoreMatchers.equalTo(givenMessage))

        coVerify(atLeast = 1) { apiService.loadPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }

    @Test
    fun `test loadPhotos() gives network error`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(stringUtils, apiService)
        val givenMessage = "Test Error Message"
        val body = givenMessage.toResponseBody("text/html".toMediaTypeOrNull())
        val apiResponse = ApiResponse.error<List<PhotoModel>>(Response.error(500, body))

        // When
        coEvery { apiService.loadPhotos(any(), any(), any()) }
            .returns(apiResponse)
        coEvery { stringUtils.somethingWentWrong() }
            .returns(givenMessage)

        // Invoke
        val apiResponseFlow = repository.loadPhotos(1, 1, "")

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseFlow.count(), CoreMatchers.`is`(1))

        val apiResponseDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.instanceOf(Resource.Error::class.java))

        val errorMessage = (apiResponseDataState as Resource.Error).message
        MatcherAssert.assertThat(errorMessage, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(errorMessage, CoreMatchers.equalTo(apiResponse.message()))

        coVerify(atLeast = 1) { apiService.loadPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }

    @Test
    fun `test searchPhotos() gives list of photos`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(stringUtils, apiService)
        val apiResponse = MockTestUtil.createSearchPhotosResponse()
        val apiCall = successCall(apiResponse)

        // When
        coEvery { apiService.searchPhotos(any(), any(), any()) }
            .returns(apiCall)

        // Invoke
        val apiResponseFlow = repository.searchPhotos("", 1, 1)

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())

        val photosListDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.instanceOf(Resource.Success::class.java))

        val photosList = (photosListDataState as Resource.Success).data
        MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosList.size, CoreMatchers.`is`(apiResponse.photosList.size))

        coVerify(exactly = 1) { apiService.searchPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }
}