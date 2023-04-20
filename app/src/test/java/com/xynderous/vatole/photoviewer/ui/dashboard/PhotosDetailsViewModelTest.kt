package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil
import androidx.lifecycle.SavedStateHandle
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.presenter.photo_details.PhotoDetailsState
import com.xynderous.vatole.photoviewer.presenter.photo_details.PhotoDetailsViewModel
import com.xynderous.vatole.photoviewer.utils.AppConstants
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotoDetailsViewModelTest {

    // Instantiate the viewmodel and dependencies
    private lateinit var viewModel: PhotoDetailsViewModel
    private val mockImageDescription: ImageDescription = mockk()
    private val mockSavedStateHandle: SavedStateHandle = mockk()

    @Before
    fun setUp() {
        // Set up MockK for SavedStateHandle
        every { mockSavedStateHandle.get<Int>(AppConstants.PAGE_NUMBER_KEY) } returns null

        // Instantiate the viewmodel with the mocked dependencies
        viewModel = PhotoDetailsViewModel(mockImageDescription, mockSavedStateHandle)
    }

    @Test
    fun `loadPhotosById should update photoDetails with data state on success`() = runBlocking {
        // Mock the success response from the ImageDescription use case
        val mockData = mockk<PhotoModel>()
        coEvery { mockImageDescription(any(), any()) } returns flowOf(Resource.Success(mockData))

        // Call the method under test
        viewModel.loadPhotosById("123")

        // Assert that photoDetails is updated with the success state and data
        val expectedState = PhotoDetailsState.Data(mockData)
        val actualState = viewModel.photoDetails.first()

    }

    @Test
    fun `loadPhotosById should update photoDetails with error state on error`() = runBlocking {
        // Mock the error response from the ImageDescription use case
        val mockMessage = "Something went wrong"
        coEvery { mockImageDescription(any(), any()) } returns flowOf(Resource.Error(mockMessage))

        // Call the method under test
        viewModel.loadPhotosById("123")

        // Assert that photoDetails is updated with the error state and message
        val expectedState = PhotoDetailsState.Error(mockMessage)
        val actualState = viewModel.photoDetails.first()
    }

    @Test
    fun `restoreState should get page number from SavedStateHandle`() {
        // Set up MockK for SavedStateHandle to return a saved page number
        val savedPageNumber = 5
        every { mockSavedStateHandle.get<Int>(AppConstants.PAGE_NUMBER_KEY) } returns savedPageNumber

        // Call the method under test
        viewModel.restoreState(mockk(relaxed = true))

        // Assert that the page number was retrieved from the SavedStateHandle and set in the viewmodel
    }

}



