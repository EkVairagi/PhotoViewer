package com.xynderous.vatole.photoviewer.data.usescases

import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf


import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchPhotosTest {

    private val repository = mockk<PhotosRepository>()
    private val useCase = SearchPhotos(repository)

    @Test
    fun `invoke should return success`() = runBlocking {
        val query = "flowers"
        val pageNumber = 1
        val pageSize = 10
        val expected = Resource.Success(MockTestUtil.createSearchPhotosResponse())
        coEvery { repository.searchPhotos(query, pageNumber, pageSize) } returns flowOf(expected)

        val result = useCase(query, pageNumber, pageSize).first()

        assertEquals(expected, result)
        coVerify { repository.searchPhotos(query, pageNumber, pageSize) }
    }


}
