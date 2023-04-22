package com.xynderous.vatole.photoviewer.utils

/*sealed class Resource<T>(val data: T, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T) : Resource<T>(data, message)
}*/

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: String?, val data: T? = null) : Resource<T>()
}
