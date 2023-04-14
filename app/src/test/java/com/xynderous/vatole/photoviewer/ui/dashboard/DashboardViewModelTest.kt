package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xynderous.vatole.photoviewer.MainCoroutine
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.presenter.viewmodels.DashBoardViewModel
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DashboardViewModelTest {

    // Subject under test
    private lateinit var viewModel: DashBoardViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutine()

    @MockK
    lateinit var searchPhotosUsecase: SearchPhotos

    @MockK
    lateinit var fetchPopularPhotosUsecase: FetchPopularImages

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when HomeViewModel is initialized, popular photos are fetched`() = runBlocking {
        // Given
        val givenPhotos = MockTestUtil.createPhotos(3)

        // When
        coEvery { fetchPopularPhotosUsecase.invoke(any(), any(), any()) }
            .returns(flowOf(Resource.Success(givenPhotos)))

        // Invoke
        viewModel = DashBoardViewModel(fetchPopularPhotosUsecase, searchPhotosUsecase)

        // Then
        coVerify(exactly = 1) { fetchPopularPhotosUsecase.invoke() }
    }

    @Test
    fun `test when HomeViewModel is initialized, popular photos throwing errors`() = runBlocking {
        // Given
        val givenMessage = "Test Error Message"

        // When
        coEvery { fetchPopularPhotosUsecase.invoke(any(), any(), any()) }
            .returns(flowOf(Resource.Error(givenMessage)))

        // Invoke
        viewModel = DashBoardViewModel(fetchPopularPhotosUsecase, searchPhotosUsecase)

        // Then
        coVerify(exactly = 1) { fetchPopularPhotosUsecase.invoke() }
    }
}
