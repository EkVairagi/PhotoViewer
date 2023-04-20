package com.xynderous.vatole.photoviewer.utils

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

 fun <T> successCall(data: T): Call<T> {
    val successResponse = Response.success(data)
    return object : Call<T> {
        override fun enqueue(callback: Callback<T>) {}
        override fun isExecuted(): Boolean = false
        override fun clone(): Call<T> = this
        override fun isCanceled(): Boolean = false
        override fun cancel() {}
        override fun execute(): Response<T> = successResponse
        override fun request(): Request = Request.Builder().url("http://localhost").build()
        override fun timeout(): Timeout = Timeout().timeout(30, TimeUnit.SECONDS)
    }
}
