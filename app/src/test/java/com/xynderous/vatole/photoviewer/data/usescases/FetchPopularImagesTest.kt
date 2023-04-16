package com.xynderous.vatole.photoviewer.data.usescases

import com.xynderous.vatole.photoviewer.domain.usecases.FetchPopularImages
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class FetchPopularImagesTest {

    @MockK
    private lateinit var repository: PhotosRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `test invoking FetchPopularPhotosUsecase gives list of photos`() = runBlocking {

        // Given
        val usecase = FetchPopularImages(repository)
        val givenPhotos = MockTestUtil.createPhotos(3)

        // When
        coEvery { repository.loadPhotos(any(), any(), any()) }
            .returns(givenPhotos)

        // Invoke
        val photosListFlow = usecase(1, 1, "")

        // Then
        MatcherAssert.assertThat(photosListFlow, CoreMatchers.notNullValue())

        val photosListDataState = photosListFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())

        if (photosListDataState is Resource.Success) {
            val photosList = photosListDataState.data
            MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
            MatcherAssert.assertThat(photosList?.size, CoreMatchers.`is`(givenPhotos.size))
        } else if (photosListDataState is Resource.Error) {
            // Handle the error case
            val errorMessage = photosListDataState.message
            MatcherAssert.assertThat(errorMessage, CoreMatchers.notNullValue())
            MatcherAssert.assertThat(errorMessage, CoreMatchers.equalTo("Test Error Message"))
        }

    }

    @Test
    fun `test invoking FetchPopularPhotosUsecase gives error`() = runBlocking {

        // Given
        val usecase = FetchPopularImages(repository)
        val givenErrorMessage = "Test Error Message"

        // When

        coEvery { repository.loadPhotos(any(),any(),any()) }


        // Invoke
        val photosListFlow = usecase(1, 1, "")

        // Then
        MatcherAssert.assertThat(photosListFlow, CoreMatchers.notNullValue())

        val photosListDataState = photosListFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            photosListDataState,
            CoreMatchers.instanceOf(Resource.Error::class.java)
        )

        val errorMessage = (photosListDataState as Resource.Error).message
        MatcherAssert.assertThat(errorMessage, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(errorMessage, CoreMatchers.`is`(givenErrorMessage))
    }

}
