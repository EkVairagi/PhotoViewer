
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoUrlsModel
import com.xynderous.vatole.photoviewer.data.model.DomainSearchPhotosResponse
import com.xynderous.vatole.photoviewer.data.model.DomainUserModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoUrlsModel
import com.xynderous.vatole.photoviewer.domain.model.UserModel


class MockTestUtil {
    companion object {
        fun createPhotos(count: Int): List<DomainPhotoModel> {
            return (0 until count).map {
                DomainPhotoModel(
                    id = "$it",
                    created_at = "2016-05-03T11:00:28-04:00",
                    color = "#60544D",
                    description = "A man drinking a coffee.",
                    alt_description = "",
                    urls = createPhotoUrls(),
                    user = createUser(it)
                )
            }
        }

        fun createPhotoUrls(): DomainPhotoUrlsModel {
            return DomainPhotoUrlsModel(
                raw = "",
                full = "",
                regular = "",
                small = "",
                thumb = ""
            )
        }

        fun createUser(position: Int): DomainUserModel {
            return DomainUserModel(
                id = "$position",
                username = "username{$position}",
                location = "location{$position}",
                name = "User Full Name $position"
            )
        }

        fun createSearchPhotosResponse(): DomainSearchPhotosResponse {
            return DomainSearchPhotosResponse(
                total = 3,
                total_pages = 1,
                results = createPhotos(3)
            )
        }

        fun imageDescription(): DomainPhotoModel {
            return DomainPhotoModel(
                id = "",
                created_at = "2016-05-03T11:00:28-04:00",
                color = "#60544D",
                description = "A man drinking a coffee.",
                alt_description = "",
                urls = createPhotoUrls(),
                user = createUser(0)
            )
        }


    }
}
