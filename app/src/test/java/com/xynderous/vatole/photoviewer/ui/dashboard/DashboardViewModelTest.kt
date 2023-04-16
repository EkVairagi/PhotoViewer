package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil.Companion.createPhotos
import MockTestUtil.Companion.createSearchPhotosResponse
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.presenter.photo_dashboard.DashBoardViewModel
import com.xynderous.vatole.photoviewer.presenter.photo_dashboard.PhotoState
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DashBoardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: DashBoardViewModel

    private lateinit var fetchPopularImages: FetchPopularImages
    private lateinit var searchPhotos: SearchPhotos

    @Before
    fun setUp() {
        fetchPopularImages = mockk()
        searchPhotos = mockk()

        viewModel = DashBoardViewModel(fetchPopularImages, searchPhotos)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when fetchPhotosAPI called with valid input, should fetch photos and emit PhotoState with success state`() =
        runBlockingTest {
            // given
            coEvery { fetchPopularImages(any()) } returns flowOf(Resource.Success(createPhotos(5)))

            // when
            viewModel.fetchPhotosAPI()

            // then
            val expectedState = PhotoState(data = createPhotos(5))
            val currentState = viewModel.photoDetails.value
            assertEquals(expectedState, currentState)
        }

    @Test
    fun `when fetchPhotosAPI called with valid input, should emit PhotoState with loading state followed by success`() =
        runBlockingTest {
            // given
            coEvery { fetchPopularImages(any()) } returns flowOf(Resource.Success(createPhotos(5)))

            // when
            viewModel.fetchPhotosAPI()

            // then
            val expectedStates = listOf(
                PhotoState(isLoading = true),
                PhotoState(isLoading = false, data = createPhotos(5))
            )
            val currentStates = mutableListOf<PhotoState>()
            viewModel.photoDetails.take(expectedStates.size).collect {
                currentStates.add(it)
            }
            assertEquals(expectedStates, currentStates)
        }



    @Test
    fun `when fetchPhotosAPI called with valid input and returns error, should emit PhotoState with error state`() =
        runBlockingTest {
            // given
            coEvery { fetchPopularImages(any()) } returns flowOf(Resource.Error("Failed to fetch photos"))

            // when
            viewModel.fetchPhotosAPI()

            // then
            val expectedState = PhotoState(error = "Failed to fetch photos")
            val currentState = viewModel.photoDetails.value
            assertEquals(expectedState, currentState)
        }

    @Test
    fun `when loadMorePhotos called with valid input and searchQuery is empty, should fetch more photos and emit PhotoState with success state`() =
        runBlockingTest {
            // given
            coEvery { fetchPopularImages(any()) } returns flowOf(Resource.Success(createPhotos(5)))
            viewModel.fetchPhotosAPI()

            // when
            viewModel.loadMorePhotos()

            // then
            val expectedState = PhotoState(data = createPhotos(10))
            val currentState = viewModel.photoDetails.value
            assertEquals(expectedState, currentState)
        }

    @Test
    fun `when loadMorePhotos called with valid input and searchQuery is not empty, should search for photos and emit PhotoState with success state`() =
        runBlockingTest {
            // given


            coEvery { searchPhotos(any(), any()) } returns flowOf(Resource.Success(createSearchPhotosResponse()))
            viewModel.searchPhotos("coffee")

            // when
            viewModel.loadMorePhotos()

            // then
            val expectedState = PhotoState(data = createPhotos(10))
            val currentState = viewModel.photoDetails.value
            assertEquals(expectedState, currentState)
        }


}



