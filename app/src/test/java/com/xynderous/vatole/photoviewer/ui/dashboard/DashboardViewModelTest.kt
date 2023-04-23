package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil
import androidx.lifecycle.SavedStateHandle
import com.xynderous.vatole.photoviewer.data.model.DomainSearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.ui.photo_dashboard.DashBoardViewModel
import com.xynderous.vatole.photoviewer.utils.AppConstants
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

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
        val dataSize = 10
        val photos = MockTestUtil.createPhotos(dataSize)
        coEvery { fetchPopularPhotos(any(), any(), any()) } returns flowOf(Resource.Success(photos))

        viewModel.fetchPhotosAPI()


    }


    @Test
    fun `fetchPhotos should update the photoDetails value with loading and then error state`() =
        runBlockingTest {
            val page = 1
            val errorMessage = "Error"
            coEvery { fetchPopularPhotos(page, any(), any()) } returns flowOf(
                Resource.Error(
                    errorMessage, emptyList()
                )
            )
            viewModel.fetchPhotos(page)

        }


    @Test
    fun `restoreState should get page number from SavedStateHandle`() {
        val savedPageNumber = 5
        every { savedStateHandle.get<Int>(AppConstants.PAGE_NUMBER_KEY) } returns savedPageNumber

        viewModel.restoreState(mockk(relaxed = true))
    }


    @Test
    fun `restoreState should get query from SavedStateHandle`() {
        val query = "cat"
        every { savedStateHandle.get<String>(AppConstants.PAGE_NUMBER_KEY) } returns query
        viewModel.restoreState(mockk(relaxed = true))
    }


    @Test
    fun `searchPhotos should update the photoDetails value with loading and then success state`() =
        runBlockingTest {
            val query = "test"
            val page = 1
            val dataSize = 10
            val photos = MockTestUtil.createPhotos(3)
            coEvery { searchPhotos(query, page, any()) } returns flowOf(
                Resource.Success(
                    DomainSearchPhotosResponse(1, dataSize, photos)
                )
            )
            viewModel.searchPhotos(query)

        }

    @Test
    fun `searchPhotos should update the photoDetails value with loading and then error state`() =
        runBlockingTest {
            val query = "test"
            val page = 1
            val errorMessage = "Error"

            coEvery { searchPhotos(query, page, any()) } returns flowOf(
                Resource.Error(
                    errorMessage,
                    DomainSearchPhotosResponse(1, 1, emptyList())
                )
            )

            coEvery { searchPhotos(query, page, any()) } returns flowOf(
                Resource.Error(
                    errorMessage, DomainSearchPhotosResponse(1, 1, emptyList())
                )
            )
            viewModel.searchPhotos(query)
        }

}







