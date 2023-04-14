package com.xynderous.vatole.photoviewer.data.usescases


import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
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
class PhotoDetailsTest {

    @MockK
    private lateinit var repository: PhotosRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test invoking SearchPhotosUsecase gives list of photos`() = runBlocking {
        // Given
        val usecase = ImageDescription(repository)
        val givenPhotos = MockTestUtil.imageDescription()

        // When
        coEvery { repository.imageDescription(any(), any()) }
            .returns(givenPhotos)

        // Invoke
        val photosListFlow = usecase(1, "")

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
    }
}