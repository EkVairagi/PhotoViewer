package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xynderous.vatole.photoviewer.MainCoroutine
import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.presenter.viewmodels.DashBoardViewModel
import com.xynderous.vatole.photoviewer.presenter.viewmodels.PhotoDetailsViewModel
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
class PhotosDetailsViewModelTest {

    // Subject under test
    private lateinit var viewModel: PhotoDetailsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutine()

    @MockK
    lateinit var imageDescription: ImageDescription


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when PhotoDetailsViewModel is initialized, popular photos are fetched`() = runBlocking {
        // Given
        val givenPhotos = MockTestUtil.imageDescription()

        // When
        coEvery { imageDescription.invoke(any(), any()) }
            .returns(flowOf(Resource.Success(givenPhotos)))

        // Invoke
        viewModel = PhotoDetailsViewModel(imageDescription)

        // Then
        coVerify(exactly = 1) { imageDescription.invoke("") }
    }

    @Test
    fun `test when PhotoDetailsViewModel is initialized, popular photos throwing errors`() = runBlocking {
        // Given
        val givenMessage = "Test Error Message"

        // When
        coEvery { imageDescription.invoke(any(), any()) }
            .returns(flowOf(Resource.Error(givenMessage)))

        // Invoke
        viewModel = PhotoDetailsViewModel(imageDescription)

        // Then
        coVerify(exactly = 1) { imageDescription.invoke("") }
    }
}
