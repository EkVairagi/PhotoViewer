package com.xynderous.vatole.photoviewer.data.usescases


import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.usecases.SearchPhotos
import com.xynderous.vatole.photoviewer.domain.repositories.PhotosRepository
import com.xynderous.vatole.photoviewer.domain.usecases.ImageDescription
import com.xynderous.vatole.photoviewer.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.security.InvalidParameterException


@RunWith(JUnit4::class)
class PhotoDetailsTest {

    @MockK
    private lateinit var repository: PhotosRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }


    @Test
    fun testImageDescriptionUseCaseReturnsPhotoDetails() = runBlocking {
        // Given
        val useCase = ImageDescription(repository)
        val givenPhotoDetails = MockTestUtil.imageDescription()
        val givenId = "test_id"

        // When
        coEvery { repository.imageDescription(givenId, any()) }
            .returns(givenPhotoDetails)

        // Invoke
        val photoDetailsFlow = useCase(id = givenId)

        // Then
        assertPhotoDetailsFlowIsSuccess(photoDetailsFlow, givenPhotoDetails)
    }


    @Test
    fun testImageDescriptionUseCaseReturnsErrorForInvalidId() = runBlocking {
        // Given
        val useCase = ImageDescription(repository)
        val givenId = ""

        // When
        coEvery { repository.imageDescription(givenId, any()) }
            .throws(InvalidParameterException("Invalid ID"))

        // Invoke
        val photoDetailsFlow = useCase(id = givenId)

        // Then
        assertPhotoDetailsFlowIsError(
            photoDetailsFlow,
            message = "Invalid ID"
        )
    }


    @Test
    fun testImageDescriptionUseCaseReturnsErrorForIoException() = runBlocking {
        // Given
        val useCase = ImageDescription(repository)
        val givenId = "test_id"

        // When
        coEvery { repository.imageDescription(givenId, any()) }
            .throws(IOException("No internet connection"))

        // Invoke
        val photoDetailsFlow = useCase(id = givenId)

        // Then
        assertPhotoDetailsFlowIsError(
            photoDetailsFlow,
            message = "No internet connection"
        )
    }


    @Test

    fun testImageDescriptionUseCaseReturnsErrorForHttpException() = runBlocking {
// Given
        val useCase = ImageDescription(repository)
        val givenId = "test_id"
        coEvery { repository.imageDescription(givenId, any()) }
            .throws(HttpException(Response.error<Any>(404, ResponseBody.create(null, "Not found"))))

        // Invoke
        val photoDetailsFlow = useCase(id = givenId)

        // Then
        assertPhotoDetailsFlowIsError(
            photoDetailsFlow,
            message = "Not found"
        )
    }


    @Test
    fun testImageDescriptionUseCaseReturnsErrorForUnexpectedException() = runBlocking {
        // Given
        val useCase = ImageDescription(repository)
        val givenId = "test_id"

        // When
        coEvery { repository.imageDescription(givenId, any()) }
            .throws(RuntimeException("Something went wrong"))

        // Invoke
        val photoDetailsFlow = useCase(id = givenId)

        // Then
        assertPhotoDetailsFlowIsError(
            photoDetailsFlow,
            message = "An Unknown error occurred"
        )
    }


    private suspend fun assertPhotoDetailsFlowIsSuccess(
        photoDetailsFlow: Flow<Resource<PhotoModel>>,
        expectedPhotoDetails: PhotoModel
    ) {
        MatcherAssert.assertThat(photoDetailsFlow, CoreMatchers.notNullValue())

        val photoDetailsDataState = photoDetailsFlow.first()
        MatcherAssert.assertThat(photoDetailsDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            photoDetailsDataState,
            CoreMatchers.instanceOf(Resource.Success::class.java)
        )

        val photoDetails = (photoDetailsDataState as Resource.Success).data
        MatcherAssert.assertThat(photoDetails, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(photoDetails, CoreMatchers.`is`(expectedPhotoDetails))
    }


    private suspend fun assertPhotoDetailsFlowIsError(
        photoDetailsFlow: Flow<Resource<PhotoModel>>,
        message: String
    ) {
        MatcherAssert.assertThat(photoDetailsFlow, CoreMatchers.notNullValue())

        val photoDetailsDataState = photoDetailsFlow.first()
        MatcherAssert.assertThat(photoDetailsDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            photoDetailsDataState,
            CoreMatchers.instanceOf(Resource.Error::class.java)
        )

        val errorMessage = (photoDetailsDataState as Resource.Error).message
        MatcherAssert.assertThat(errorMessage, CoreMatchers.`is`(message))
    }
}


