package com.xynderous.vatole.photoviewer.data.usescases

import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList



import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

class SearchPhotosTest {

    private val repository = mockk<PhotosRepository>()
    private val useCase = SearchPhotos(repository)

    @Test
    fun `invoke should return success`() = runBlocking {
        // Given
        val query = "flowers"
        val pageNumber = 1
        val pageSize = 10
        val expected = Resource.Success(MockTestUtil.createSearchPhotosResponse())
        coEvery { repository.searchPhotos(query, pageNumber, pageSize) } returns flowOf(expected)

        // When
        val result = useCase(query, pageNumber, pageSize).first()

        // Then
        assertEquals(expected, result)
        coVerify { repository.searchPhotos(query, pageNumber, pageSize) }
    }



}
