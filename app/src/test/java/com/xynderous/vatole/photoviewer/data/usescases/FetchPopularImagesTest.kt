package com.xynderous.vatole.photoviewer.data.usescases

import MockTestUtil
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FetchPopularImagesTest {
    val repository = mockk<PhotosRepository>()
    private val useCase = FetchPopularImages(repository)

    @Test
    fun `fetch popular images - success`() = runBlockingTest {
        // Given
        // When
        val photos = MockTestUtil.createPhotos(10)
        coEvery { repository.loadPhotos(any(), any(), any()) } returns flowOf(Resource.Success(photos))
        val result = useCase(1, 10, "popular").toList()

        // Then
        verify { repository.loadPhotos(1, 10, "popular") }
        assertTrue(result.size == 1)
        assertTrue(result[0] is Resource.Success)
        assertEquals(photos, (result[0] as Resource.Success<List<PhotoModel>>).data)
    }

    @Test
    fun `fetch popular images - error`() = runBlockingTest {
        // Given
        val errorMsg = "Error fetching popular images"
        coEvery { repository.loadPhotos(any(), any(), any()) } returns flowOf(Resource.Error(errorMsg))

        // When
        val result = useCase(1, 10, "popular").toList()

        // Then
        verify { repository.loadPhotos(1, 10, "popular") }
        assertTrue(result.size == 1)
        assertTrue(result[0] is Resource.Error)
        assertEquals(errorMsg, (result[0] as Resource.Error).message)
    }
}


