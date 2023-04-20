package com.xynderous.vatole.photoviewer.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.presenter.photo_details.PhotoDetailsState
import com.xynderous.vatole.photoviewer.presenter.photo_details.PhotoDetailsViewModel
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotoDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    private lateinit var imageDescription: ImageDescription
    private lateinit var viewModel: PhotoDetailsViewModel

    @Before
    fun setup() {
        imageDescription = mockk()
        viewModel = PhotoDetailsViewModel(imageDescription)
    }

    @Test
    fun `loadPhotosById should fetch photos and update photoDetails state`() = testCoroutineScope.runBlockingTest {
        // Given
        val photoModel = MockTestUtil.imageDescription()
        val mockFlow = flow { emit(Resource.Success(photoModel)) }
        coEvery { imageDescription(any(), any()) } returns mockFlow

        // When
        viewModel.loadPhotosById("photoId")
        viewModel.photoDetails.collect {
            // Then
            assertEquals(PhotoDetailsState(data = photoModel), it)
        }
    }

    @Test
    fun `loadPhotosById should handle loading state`() = testCoroutineScope.runBlockingTest {
        // Given

        val mockFlow = flow<Resource<PhotoModel>> { emit(Resource.Loading()) }

        coEvery { imageDescription(any(), any()) } returns mockFlow

        // When
        viewModel.loadPhotosById("photoId")
        viewModel.photoDetails.collect {
            // Then
            assertEquals(PhotoDetailsState(isLoading = true), it)
        }
    }

    @Test
    fun `loadPhotosById should handle error state`() = testCoroutineScope.runBlockingTest {
        // Given
        val errorMessage = "Error message"
        val mockFlow = flow<Resource<PhotoModel>>  { emit(Resource.Error(errorMessage)) }

        coEvery { imageDescription(any(), any()) } returns mockFlow

        // When
        viewModel.loadPhotosById("photoId")
        viewModel.photoDetails.collect {
            // Then
            assertEquals(PhotoDetailsState(error = errorMessage), it)
        }
    }
}

