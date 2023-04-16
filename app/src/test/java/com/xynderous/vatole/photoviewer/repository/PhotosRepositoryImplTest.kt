package com.xynderous.vatole.photoviewer.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xynderous.vatole.photoviewer.MainCoroutine
import com.xynderous.vatole.photoviewer.data.api.PhotosAPI
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.data.repositories.PhotosRepositoryImpl
import com.xynderous.vatole.photoviewer.utils.Resource
import com.xynderous.vatole.photoviewer.utils.toDomainSearchPhotos
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
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
import retrofit2.HttpException
import retrofit2.Response

@RunWith(JUnit4::class)
class PhotosRepositoryImplTest {

    // Subject under test
    private lateinit var repository: PhotosRepositoryImpl

    @MockK
    private lateinit var apiService: PhotosAPI


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
    fun `searchPhotos() returns SearchPhotosResponse`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(apiService)
        val expectedResponse = MockTestUtil.createSearchPhotosResponse()
        val query = "test"
        val pageNumber = 1
        val pageSize = 10

        coEvery { apiService.searchPhotos(query, pageNumber, pageSize) } returns expectedResponse.toDomainSearchPhotos()

        // When
        val actualResponse = repository.searchPhotos(query, pageNumber, pageSize)

        // Then
        coVerify(exactly = 1) { apiService.searchPhotos(query, pageNumber, pageSize) }
        confirmVerified(apiService)

        MatcherAssert.assertThat(actualResponse, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(actualResponse.total, CoreMatchers.`is`(expectedResponse.total))
        MatcherAssert.assertThat(actualResponse.totalPages, CoreMatchers.`is`(expectedResponse.totalPages))
        MatcherAssert.assertThat(actualResponse.photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(actualResponse.photosList.size, CoreMatchers.`is`(expectedResponse.photosList.size))
    }

    @Test
    fun `searchPhotos() returns error if API call fails`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(apiService)
        val expectedErrorMessage = "Error message"
        val query = "test"
        val pageNumber = 1
        val pageSize = 10

        coEvery { apiService.searchPhotos(query, pageNumber, pageSize) } throws Exception(expectedErrorMessage)

        // When
        val actualResponse = repository.searchPhotos(query, pageNumber, pageSize)

        // Then
        coVerify(exactly = 1) { apiService.searchPhotos(query, pageNumber, pageSize) }
        confirmVerified(apiService)

        MatcherAssert.assertThat(actualResponse, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(actualResponse.total, CoreMatchers.`is`(0))
        MatcherAssert.assertThat(actualResponse.totalPages, CoreMatchers.`is`(0))
        MatcherAssert.assertThat(actualResponse.photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(actualResponse.photosList.size, CoreMatchers.`is`(0))
        MatcherAssert.assertThat(actualResponse.errors, CoreMatchers.`is`(expectedErrorMessage))
    }


    @Test
    fun `test loadPhotos() gives empty list of photos`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(apiService)
        val givenPhotosList = MockTestUtil.createPhotos(0)
        val apiCall = givenPhotosList

        // When
        coEvery { apiService.loadPhotos(any(), any(), any()) }.returns(apiCall)

        // Invoke
        val apiResponseFlow = repository.loadPhotos(1, 1, "")

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())

        val photosListDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            photosListDataState,
            CoreMatchers.instanceOf(Resource.Success::class.java)
        )

        val photosList = (photosListDataState as Resource.Success<*>).data
        MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosList, CoreMatchers.`is`(givenPhotosList.size))

        coVerify(exactly = 1) { apiService.loadPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }



    @Test
    fun `test loadPhotos() throws exception`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl( apiService)
        val givenMessage = "Test Error Message"

        val apiResponse = listOf<PhotoModel>()

        // When
        coEvery { apiService.loadPhotos(any(), any(), any()) }
            .returns(apiResponse)


        // Invoke
        val apiResponseFlow = repository.loadPhotos(1, 1, "")

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseFlow.count(), CoreMatchers.`is`(1))

        val apiResponseDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            apiResponseDataState,
            CoreMatchers.instanceOf(Resource.Error::class.java)
        )

        val errorMessage = (apiResponseDataState as Resource.Error<*>).message
        MatcherAssert.assertThat(errorMessage, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(errorMessage, CoreMatchers.equalTo(givenMessage))

        coVerify(atLeast = 1) { apiService.loadPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }


    @Test
    fun `test loadPhotos() gives network error`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl(apiService)
        val givenMessage = "Test Error Message"
        val errorResponse = Response.error<List<PhotoModel>>(400, givenMessage.toResponseBody("text/plain".toMediaTypeOrNull()))
        val exception = HttpException(errorResponse)

        // When
        coEvery { apiService.loadPhotos(any(), any(), any()) }
            .throws(exception)


        // Invoke
        val apiResponseFlow = repository.loadPhotos(1, 1, "")

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseFlow.count(), CoreMatchers.`is`(1))

        val apiResponseDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            apiResponseDataState,
            CoreMatchers.instanceOf(Resource.Error::class.java)
        )

        val errorMessage = (apiResponseDataState as Resource.Error<*>).message
        MatcherAssert.assertThat(errorMessage, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(errorMessage, CoreMatchers.equalTo(givenMessage))

        coVerify(atLeast = 1) { apiService.loadPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }


    @Test
    fun `test searchPhotos() gives list of photos`() = runBlocking {
        // Given
        repository = PhotosRepositoryImpl( apiService)
        val apiResponse = MockTestUtil.createSearchPhotosResponse()
        val apiCall = apiResponse

        // When
        coEvery { apiService.searchPhotos(any(), any(), any()) }
            .returns(apiCall)

        // Invoke
        val apiResponseFlow = repository.searchPhotos("", 1, 1)

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())

        val photosListDataState = apiResponseFlow
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            photosListDataState,
            CoreMatchers.instanceOf(Resource.Success::class.java)
        )

        val photosList = (photosListDataState as Resource.Success<*>).data
        MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosList, CoreMatchers.`is`(apiResponse.photosList.size))

        coVerify(exactly = 1) { apiService.searchPhotos(any(), any(), any()) }
        confirmVerified(apiService)
    }
}