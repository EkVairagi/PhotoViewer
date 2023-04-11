package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.xynderous.vatole.photoviewer.MainCoroutine
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.presenter.viewmodels.PhotoDetailsViewModel
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PhotoDetailsViewModelTest {

    // Subject under test
    private lateinit var viewModel: PhotoDetailsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutine()

    @MockK
    lateinit var imageDescriptionUseCases: ImageDescription

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `test when PhotoDetailsViewModel is initialized, image description are fetched`() = runBlocking {
        // Given
        val givenPhotos = MockTestUtil.imageDescription()

        // When
        coEvery { imageDescriptionUseCases.invoke(any(), any()) }
            .returns(flowOf(Resource.success(givenPhotos)))

        // Invoke
        viewModel = PhotoDetailsViewModel(imageDescriptionUseCases)

        viewModel.photoModelLiveDataByAPI.collect()

        // Then
        coVerify(exactly = 1) { imageDescriptionUseCases.invoke(any()) }
    }
}