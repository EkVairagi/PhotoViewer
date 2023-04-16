package com.xynderous.vatole.photoviewer.data.usescases

import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
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

@RunWith(JUnit4::class)
class SearchPhotoTest {

    @MockK
    private lateinit var repository: PhotosRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }


/*
    @Test
    fun `test invoking SearchPhotosUsecase gives list of photos`() = runBlocking {
        // Given
        val usecase = SearchPhotos(repository)
        val givenPhotos = MockTestUtil.createSearchPhotosResponse()

        // When
        coEvery { repository.searchPhotos(any(), any(), any()) }
            .returns(givenPhotos)

        // Invoke
        val photosListFlow = usecase("", 1, 1)

        // Then
        MatcherAssert.assertThat(photosListFlow, CoreMatchers.notNullValue())

        val photosListDataState = photosListFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            photosListDataState,
            CoreMatchers.instanceOf(Resource.Success::class.java)
        )

        val photosList = (photosListDataState as Resource.Success).data
        MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosList, CoreMatchers.`is`(givenPhotos))
        MatcherAssert.assertThat(photosList, CoreMatchers.`is`(givenPhotos))
    }
*/



    @Test
    fun `test invoking SearchPhotosUsecase gives list of photos`() = runBlocking {
        // Given
        val usecase = SearchPhotos(repository)
        val givenPhotos = MockTestUtil.createSearchPhotosResponse()

        // When
        coEvery { repository.searchPhotos(any(), any(), any()) }
            .returns(givenPhotos)

        // Invoke
        val photosListFlow = usecase("", 1, 1)

        // Then
        MatcherAssert.assertThat(photosListFlow, CoreMatchers.notNullValue())

        val photosListDataState = photosListFlow.first()
        MatcherAssert.assertThat(photosListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            photosListDataState,
            CoreMatchers.instanceOf(Resource.Success::class.java)
        )

        val photosList = (photosListDataState as Resource.Success).data?.photosList
        MatcherAssert.assertThat(photosList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photosList?.size, CoreMatchers.`is`(givenPhotos.photosList.size))
        MatcherAssert.assertThat(photosList, CoreMatchers.`is`(givenPhotos.photosList))
    }

    @Test
    fun `test invoking SearchPhotosUsecase with empty query gives error`() = runBlocking {
        // Given
        val usecase = SearchPhotos(repository)
        val givenErrorMessage = "Query cannot be empty"

        // When
        coEvery { repository.searchPhotos(any(), any(), any()) }

        val photosListFlow = usecase("", 1, 1)

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