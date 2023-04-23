package com.xynderous.vatole.photoviewer.repository

import MockTestUtil
import com.xynderous.vatole.photoviewer.data.api.PhotosAPI
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.data.model.DomainSearchPhotosResponse
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
    private val repository = PhotosRepositoryImpl(photosApi)

    @Test
    fun `loadPhotos returns Resource_Success with photos list`() = runBlocking {
        val expected = listOf<DomainPhotoModel>(mockk(), mockk(), mockk())
        coEvery { photosApi.loadPhotos(any(), any(), any()) } returns expected
        coVerify { photosApi.loadPhotos(1, 10, "latest") }
    }

    @Test
    fun `searchPhotos returns Resource_Success with search result`() = runBlocking {
        val expected = mockk<DomainSearchPhotosResponse>()
        coEvery { photosApi.searchPhotos(any(), any(), any()) } returns expected
        coVerify { photosApi.searchPhotos("cats", 1, 10) }
    }

    @Test
    fun `imageDescription returns Resource_Success with photo model`() = runBlocking {
        val expected = mockk<DomainPhotoModel>()
        coEvery { photosApi.imageDescription(any(), any()) } returns expected
        coVerify { photosApi.imageDescription("abc123", 1) }
    }


    @Test
    fun `loadPhotos returns Resource Error when API call fails`() = runBlockingTest {
        val pageNumber = 1
        val pageSize = 10
        val orderBy = "latest"
        val errorMessage = "Failed to load photos"

        coEvery { photosApi.loadPhotos(pageNumber, pageSize, orderBy) } throws Exception(
            errorMessage
        )
    }

    @Test
    fun `searchPhotos returns Resource Error when API call fails`() = runBlockingTest {
        val query = "dog"
        val pageNumber = 1
        val pageSize = 10
        val errorMessage = "Failed to search photos"

        coEvery { photosApi.searchPhotos(query, pageNumber, pageSize) } throws Exception(
            errorMessage
        )
    }

    @Test
    fun `imageDescription returns Resource Error when API call fails`() = runBlockingTest {
        val id = "abc123"
        val pageNumber = 1
        val errorMessage = "Failed to get photo description"
        coEvery { photosApi.imageDescription(id, pageNumber) } throws Exception(errorMessage)
    }

}




