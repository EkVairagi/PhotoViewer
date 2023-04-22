package com.xynderous.vatole.photoviewer.data.usescases


import MockTestUtil
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test


class ImageDescriptionTest {

    // Mock dependencies
    private val repository: PhotosRepository = mockk()

    // Create instance of use case with mocked dependencies
    private val imageDescription = ImageDescription(repository)

    @Test
    fun `imageDescription returns success`() = runBlocking {
        // Given
        val id = "1NCcWi24FRs"
        val pageNumber = 1
        val expected = Resource.Success(MockTestUtil.imageDescription())
        coEvery { repository.imageDescription(id, pageNumber) } returns flowOf(expected)

        // When
        val result = imageDescription(id, pageNumber).first()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `imageDescription returns error`() = runBlocking {
        // Given
        val id = "1NCcWi24FRs"
        val pageNumber = 1
        val expected: Resource<DomainPhotoModel> = Resource.Error("Error message")

        coEvery { repository.imageDescription(id, pageNumber) } returns flowOf(expected)

        // When
        val result = imageDescription(id, pageNumber).first()

        // Then
        assertEquals(expected, result)
    }

}



