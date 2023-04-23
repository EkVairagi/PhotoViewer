package com.xynderous.vatole.photoviewer.ui.dashboard

import MockTestUtil.Companion.createPhotoUrls
import MockTestUtil.Companion.createUser
import androidx.lifecycle.SavedStateHandle
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.ui.base.BaseState
import com.xynderous.vatole.photoviewer.ui.photo_details.PhotoDetailsViewModel
import com.xynderous.vatole.photoviewer.utils.AppConstants
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotoDetailsViewModelTest {

    private lateinit var viewModel: PhotoDetailsViewModel
    private val mockImageDescription: ImageDescription = mockk()
    private val mockSavedStateHandle: SavedStateHandle = mockk()

    @Before
    fun setUp() {
        every { mockSavedStateHandle.get<Int>(AppConstants.PAGE_NUMBER_KEY) } returns null
        viewModel = PhotoDetailsViewModel(mockImageDescription, mockSavedStateHandle)
    }

    @Test
    fun `loadPhotosById should update photoDetails with data state on success`() = runBlocking {
        val mockData = mockk<DomainPhotoModel>()
        coEvery { mockImageDescription(any(), any()) } returns flowOf(Resource.Success(mockData))
        viewModel.loadPhotosById("123")

    }

    @Test
    fun `loadPhotosById should update photoDetails with error state on error`() = runBlocking {
        val mockMessage = "Something went wrong"
        coEvery { mockImageDescription(any(), any()) } returns flowOf(
            Resource.Error(
                mockMessage, DomainPhotoModel(
                    id = "1",
                    created_at = "2016-05-03T11:00:28-04:00",
                    color = "#60544D",
                    description = "A man drinking a coffee.",
                    alt_description = "",
                    urls = createPhotoUrls(),
                    user = createUser(1)
                )
            )
        )
        viewModel.loadPhotosById("123")
    }

    @Test
    fun `restoreState should get page number from SavedStateHandle`() {
        val savedPageNumber = 5
        every { mockSavedStateHandle.get<Int>(AppConstants.PAGE_NUMBER_KEY) } returns savedPageNumber
        viewModel.restoreState(mockk(relaxed = true))
    }

}



