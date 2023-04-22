package com.xynderous.vatole.photoviewer.data.mapper

import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoUrlsModel
import com.xynderous.vatole.photoviewer.data.model.DomainUserModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoUrlsModel
import com.xynderous.vatole.photoviewer.domain.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.model.UserModel
import com.xynderous.vatole.photoviewer.utils.toDomainPhotoUrls
import com.xynderous.vatole.photoviewer.utils.toDomainPhotos
import com.xynderous.vatole.photoviewer.utils.toDomainSearchPhoto
import com.xynderous.vatole.photoviewer.utils.toDomainUser
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest {
    @Test
    fun `toDomainPhotos should map PhotoModel to DomainPhotoModel`() {
        val photoModel = mockk<PhotoModel>()
        every { photoModel.id } returns "123"
        every { photoModel.created_at } returns "2022-04-22T12:34:56"
        every { photoModel.color } returns "FFFFFF"
        every { photoModel.description } returns "A beautiful sunset"
        every { photoModel.alt_description } returns "An orange and pink sky over the horizon"
        val urlsModel = mockk<PhotoUrlsModel>()
        every { urlsModel.raw } returns "https://example.com/photo.jpg"
        every { urlsModel.full } returns "https://example.com/photo-full.jpg"
        every { urlsModel.regular } returns "https://example.com/photo-regular.jpg"
        every { urlsModel.small } returns "https://example.com/photo-small.jpg"
        every { urlsModel.thumb } returns "https://example.com/photo-thumb.jpg"
        every { photoModel.urls } returns urlsModel
        val userModel = mockk<UserModel>()
        every { userModel.id } returns "456"
        every { userModel.username } returns "johndoe"
        every { userModel.location } returns "New York, USA"
        every { userModel.name } returns "John Doe"
        every { photoModel.user } returns userModel

        val domainPhotoModel = photoModel.toDomainPhotos()

        assertEquals("123", domainPhotoModel.id)
        assertEquals("2022-04-22T12:34:56", domainPhotoModel.created_at)
        assertEquals("FFFFFF", domainPhotoModel.color)
        assertEquals("A beautiful sunset", domainPhotoModel.description)
        assertEquals("An orange and pink sky over the horizon", domainPhotoModel.alt_description)
        assertEquals("https://example.com/photo.jpg", domainPhotoModel.urls?.raw)
        assertEquals("https://example.com/photo-full.jpg", domainPhotoModel.urls?.full)
        assertEquals("https://example.com/photo-regular.jpg", domainPhotoModel.urls?.regular)
        assertEquals("https://example.com/photo-small.jpg", domainPhotoModel.urls?.small)
        assertEquals("https://example.com/photo-thumb.jpg", domainPhotoModel.urls?.thumb)
        assertEquals("456", domainPhotoModel.user?.id)
        assertEquals("johndoe", domainPhotoModel.user?.username)
        assertEquals("New York, USA", domainPhotoModel.user?.location)
        assertEquals("John Doe", domainPhotoModel.user?.name)
    }


    @Test
    fun `toDomainPhotoUrls should map PhotoUrlsModel to DomainPhotoUrlsModel`() {
        val urlsModel = mockk<PhotoUrlsModel>()
        every { urlsModel.raw } returns "https://example.com/photo.jpg"
        every { urlsModel.full } returns "https://example.com/photo-full.jpg"
        every { urlsModel.regular } returns "https://example.com/photo-regular.jpg"
        every { urlsModel.small } returns "https://example.com/photo-small.jpg"
        every { urlsModel.thumb } returns "https://example.com/photo-thumb.jpg"

        val domainPhotoUrlsModel = urlsModel.toDomainPhotoUrls()

        assertEquals("https://example.com/photo.jpg", domainPhotoUrlsModel.raw)
        assertEquals("https://example.com/photo-full.jpg", domainPhotoUrlsModel.full)
        assertEquals("https://example.com/photo-regular.jpg", domainPhotoUrlsModel.regular)
        assertEquals("https://example.com/photo-small.jpg", domainPhotoUrlsModel.small)
        assertEquals("https://example.com/photo-thumb.jpg", domainPhotoUrlsModel.thumb)
    }

    @Test
    fun `toDomainPhotoUrls should map PhotoUrlsModel to DomainPhotoUrlsModel with null values`() {
        val urlsModel = mockk<PhotoUrlsModel>()
        every { urlsModel.raw } returns null
        every { urlsModel.full } returns null
        every { urlsModel.regular } returns null
        every { urlsModel.small } returns null
        every { urlsModel.thumb } returns null

        val domainPhotoUrlsModel = urlsModel.toDomainPhotoUrls()

        assertEquals("", domainPhotoUrlsModel.raw)
        assertEquals("", domainPhotoUrlsModel.full)
        assertEquals("", domainPhotoUrlsModel.regular)
        assertEquals("", domainPhotoUrlsModel.small)
        assertEquals("", domainPhotoUrlsModel.thumb)
    }


    @Test
    fun `toDomainUser should map UserModel to DomainUserModel`() {
        val userModel = mockk<UserModel>()
        every { userModel.id } returns "123"
        every { userModel.username } returns "johndoe"
        every { userModel.location } returns "New York, NY"
        every { userModel.name } returns "John Doe"

        val domainUserModel = userModel.toDomainUser()

        assertEquals("123", domainUserModel.id)
        assertEquals("johndoe", domainUserModel.username)
        assertEquals("New York, NY", domainUserModel.location)
        assertEquals("John Doe", domainUserModel.name)
    }

    @Test
    fun `toDomainUser should map UserModel to DomainUserModel with null values`() {
        val userModel = mockk<UserModel>()
        every { userModel.id } returns null
        every { userModel.username } returns null
        every { userModel.location } returns null
        every { userModel.name } returns null

        val domainUserModel = userModel.toDomainUser()

        assertEquals("", domainUserModel.id)
        assertEquals("", domainUserModel.username)
        assertEquals("", domainUserModel.location)
        assertEquals("", domainUserModel.name)
    }



}


