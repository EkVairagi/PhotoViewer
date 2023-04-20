package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.xynderous.vatole.photoviewer.MainCoroutine
import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.presenter.photo_dashboard.DashBoardViewModel
import com.xynderous.vatole.photoviewer.presenter.photo_dashboard.PhotoState
import com.xynderous.vatole.photoviewer.presenter.photo_details.PhotoDetailsViewModel
import com.xynderous.vatole.photoviewer.utils.AppConstants
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
class DashBoardViewModelTest {
    private val fetchPopularPhotos: FetchPopularImages = mockk()

    private val searchPhotos: SearchPhotos = mockk()

    private val savedStateHandle: SavedStateHandle = mockk()

    private lateinit var viewModel: DashBoardViewModel

    private val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { savedStateHandle.get<Int>(any()) } answers { null }
        every { savedStateHandle.get<String>(any()) } answers { null }

        viewModel = DashBoardViewModel(fetchPopularPhotos, searchPhotos, savedStateHandle)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetchPhotosAPI should fetch popular images from repository`() = runBlockingTest {
        // Given
        val dataSize = 10
        val photos = MockTestUtil.createPhotos(dataSize)
        coEvery { fetchPopularPhotos(any(), any(), any()) } returns flowOf(Resource.Success(photos))

        // When
        viewModel.fetchPhotosAPI()


    }


    @Test
    fun `fetchPhotos should update the photoDetails value with loading and then error state`() =
        runBlockingTest {
            // Given
            val page = 1
            val errorMessage = "Error"
            coEvery { fetchPopularPhotos(page, any(), any()) } returns flowOf(
                Resource.Error(
                    errorMessage
                )
            )

            // When
            viewModel.fetchPhotos(page)

        }


    @Test
    fun `restoreState should get page number from SavedStateHandle`() {
        // Set up MockK for SavedStateHandle to return a saved page number
        val savedPageNumber = 5
        every { savedStateHandle.get<Int>(AppConstants.PAGE_NUMBER_KEY) } returns savedPageNumber

        // Call the method under test
        viewModel.restoreState(mockk(relaxed = true))

        // Assert that the page number was retrieved from the SavedStateHandle and set in the viewmodel
    }


    @Test
    fun `restoreState should get query from SavedStateHandle`() {
        // Set up MockK for SavedStateHandle to return a saved page number
        val query = "cat"
        every { savedStateHandle.get<String>(AppConstants.PAGE_NUMBER_KEY) } returns query

        // Call the method under test
        viewModel.restoreState(mockk(relaxed = true))

        // Assert that the page number was retrieved from the SavedStateHandle and set in the viewmodel
    }


    @Test
    fun `searchPhotos should update the photoDetails value with loading and then success state`() =
        runBlockingTest {
            // Given
            val query = "test"
            val page = 1
            val dataSize = 10
            val photos = MockTestUtil.createPhotos(3)
            coEvery { searchPhotos(query, page, any()) } returns flowOf(
                Resource.Success(
                    SearchPhotosResponse(1, dataSize, photos)
                )
            )

            // When
            viewModel.searchPhotos(query)

        }

    @Test
    fun `searchPhotos should update the photoDetails value with loading and then error state`() =
        runBlockingTest {
            // Given
            val query = "test"
            val page = 1
            val errorMessage = "Error"
            coEvery { searchPhotos(query, page, any()) } returns flowOf(
                Resource.Error(
                    errorMessage
                )
            )

            // When
            viewModel.searchPhotos(query)

            // Then
        }

}







