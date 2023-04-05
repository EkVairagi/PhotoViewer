
import com.xynderous.vatole.photoviewer.data.model.SearchPhotosResponse
import com.xynderous.vatole.photoviewer.domain.model.PhotoModel
import com.xynderous.vatole.photoviewer.domain.model.PhotoUrlsModel
import com.xynderous.vatole.photoviewer.domain.model.UserModel


class MockTestUtil {
    companion object {
        fun createPhotos(count: Int): List<PhotoModel> {
            return (0 until count).map {
                PhotoModel(
                    id = "$it",
                    created_at = "2016-05-03T11:00:28-04:00",
                    color = "#60544D",
                    description = "A man drinking a coffee.",
                    urls = createPhotoUrls(),
                    user = createUser(it)
                )
            }
        }

        fun createPhotoUrls(): PhotoUrlsModel {
            return PhotoUrlsModel(
                raw = "",
                full = "",
                regular = "",
                small = "",
                thumb = ""
            )
        }

        fun createUser(position: Int): UserModel {
            return UserModel(
                id = "$position",
                username = "username{$position}",
                name = "User Full Name $position"
            )
        }

        fun createSearchPhotosResponse(): SearchPhotosResponse {
            return SearchPhotosResponse(
                total = 3,
                totalPages = 1,
                photosList = createPhotos(3)
            )
        }

        fun imageDescription(): PhotoModel {
            return PhotoModel(
                id = "",
                created_at = "2016-05-03T11:00:28-04:00",
                color = "#60544D",
                description = "A man drinking a coffee.",
                urls = createPhotoUrls(),
            )
        }


    }
}
