package com.xynderous.vatole.photoviewer.repository

import MockTestUtil
import com.xynderous.vatole.photoviewer.data.api.PhotosAPI
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.data.repositories.PhotosRepositoryImpl
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException


class PhotosRepositoryImplTest {

    private val photosApi = mockk<PhotosAPI>()
    private var photosRepository = PhotosRepositoryImpl(photosApi)

    @Before
    fun setup() {
        photosRepository = PhotosRepositoryImpl(photosApi)
    }

    @Test
    fun `test loadPhotos() gives list of photos`() = runBlockingTest {
        // Given
        val photos = MockTestUtil.createPhotos(2)

        coEvery { photosApi.loadPhotos(any(),any(),any()) } returns photos

        val repository = PhotosRepositoryImpl(photosApi)
        val result = repository.loadPhotos(1, 10, "popular")

    }


    @Test
    fun `loadPhotos should return loading resource before fetching photos`() = runBlockingTest {
        // Given
        val pageNumber = 1
        val pageSize = 10
        val orderBy = "latest"
        coEvery { photosApi.loadPhotos(pageNumber, pageSize, orderBy) } coAnswers {
            delay(1000)
            MockTestUtil.createPhotos(pageSize)
        }

        // When
        val actualPhotos = photosRepository.loadPhotos(pageNumber, pageSize, orderBy).take(2).toList()

        // Then
        verify { runBlocking { photosApi.loadPhotos(pageNumber, pageSize, orderBy) } }
        assertTrue(actualPhotos[0] is Resource.Loading)
        assertTrue(actualPhotos[1] is Resource.Success)
    }

    @Test
    fun `loadPhotos should return error resource if API call fails`() = runBlocking{
        // Given
        val pageNumber = 1
        val pageSize = 10
        val orderBy = "latest"
        val errorMessage = "Error fetching photos"
        coEvery { photosApi.loadPhotos(pageNumber, pageSize, orderBy) } throws Exception(errorMessage)
        // When
        val actualPhotos = photosRepository.loadPhotos(pageNumber, pageSize, orderBy).first()
        // Then

    }

    @Test
    fun `searchPhotos returns Resource Success when API call is successful`() = runBlocking {
        // Given
        val query = "coffee"
        val pageNumber = 1
        val pageSize = 10
        val expectedSearchPhotosResponse = MockTestUtil.createSearchPhotosResponse()
        coEvery {
            photosApi.searchPhotos(
                query,
                pageNumber,
                pageSize
            )
        } returns expectedSearchPhotosResponse

        // When
        val result = photosRepository.searchPhotos(query, pageNumber, pageSize).toList()

        // Then
    }

    @Test
    fun `searchPhotos returns Resource Error when API call fails`() = runBlocking {
        // Given
        val query = "coffee"
        val pageNumber = 1
        val pageSize = 10
        val expectedException = Exception("API call failed")
        coEvery { photosApi.searchPhotos(query, pageNumber, pageSize) } returns SearchPhotosResponse(
            0, 0, emptyList(),
        )
        val result = photosRepository.searchPhotos(query, pageNumber, pageSize).toList()

    }



    @Test
    fun `imageDescription returns Resource Success when API call is successful`() = runBlocking {
        // Given
        val id = "1"
        val pageNumber = 1
        val expectedPhotoModel = MockTestUtil.imageDescription()
        coEvery { photosApi.imageDescription(id, pageNumber) } returns expectedPhotoModel
        // When
        val result = photosRepository.imageDescription(id, pageNumber).toList()

        // Then
        coVerify { photosApi.imageDescription(id, pageNumber) }
    }

    @Test
    fun `imageDescription returns Resource Error when API call throws exception`() = runBlocking {
        // Given
        val id = "1"
        val pageNumber = 1
        val expectedException = IOException("Error loading image description")
        coEvery { photosApi.imageDescription(id, pageNumber) } throws expectedException


        coEvery { photosApi.imageDescription(id, pageNumber) } returns PhotoModel()

        // When
        val result = photosRepository.imageDescription(id, pageNumber).toList()

        // Then
        coVerify { photosApi.imageDescription(id, pageNumber) }
    }
}



