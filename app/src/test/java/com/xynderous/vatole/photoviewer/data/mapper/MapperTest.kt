package com.xynderous.vatole.photoviewer.data.mapper

import com.xynderous.vatole.photoviewer.data.model.PhotoModel
import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.utils.toDomainPhotos
import com.xynderous.vatole.photoviewer.utils.toDomainSearchPhotos
import org.junit.Assert.assertEquals
import org.junit.Test


class MapperTest {

    @Test
    fun `PhotoModel to DomainPhotos mapper test`() {
        // Arrange
        val photoModel = PhotoModel(
            id = "123",
            alt_description = "A beautiful scenery",
            description = "A picture of a mountain range",
            color = "#FFFFFF",
            created_at = "2022-04-22",
            urls = null,
            user = null
        )

        // Act
        val domainPhoto = photoModel.toDomainPhotos()

        // Assert
        assertEquals("123", domainPhoto.id)
        assertEquals("A beautiful scenery", domainPhoto.alt_description)
        assertEquals("A picture of a mountain range", domainPhoto.description)
        assertEquals("#FFFFFF", domainPhoto.color)
        assertEquals("2022-04-22", domainPhoto.created_at)
    }

    @Test
    fun `SearchPhotosResponse to DomainSearchPhotos mapper test`() {
        // Arrange
        val photo1 = PhotoModel(
            id = "123",
            alt_description = "A beautiful scenery",
            description = "A picture of a mountain range",
            color = "#FFFFFF",
            created_at = "2022-04-22",
            urls = null,
            user = null
        )

        val photo2 = PhotoModel(
            id = "456",
            alt_description = "A sunset on the beach",
            description = "A picture of a beach with a beautiful sunset",
            color = "#000000",
            created_at = "2022-04-20",
            urls = null,
            user = null
        )

        val searchPhotosResponse = SearchPhotosResponse(
            total = 2,
            totalPages = 1,
            photosList = listOf(photo1, photo2)
        )

        // Act
        val domainSearchPhotos = searchPhotosResponse.toDomainSearchPhotos()

        // Assert
        assertEquals(2, domainSearchPhotos.total)
        assertEquals(1, domainSearchPhotos.totalPages)
        assertEquals(2, domainSearchPhotos.photosList.size)
        assertEquals(photo1, domainSearchPhotos.photosList[0])
        assertEquals(photo2, domainSearchPhotos.photosList[1])
    }
}
