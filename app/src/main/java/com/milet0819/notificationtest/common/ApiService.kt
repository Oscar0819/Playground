package com.milet0819.notificationtest.common

import com.milet0819.notificationtest.common.dto.Post
import com.milet0819.notificationtest.common.dto.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): Response<Post>

    @GET("users")
    suspend fun getUsers(): Response<Users>
}



//suspend fun <T> Call<T>.awaitResponse(): T = suspendCoroutine { continuation ->
//    enqueue(object : Callback<T> {
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//            if (response.isSuccessful) {
//                response.body()?.let { body ->
//                    continuation.resume(body) // 성공시 데이터 반환
//                } ?: {
//                    continuation.resumeWithException(NullPointerException("Response body is null"))
//                }
//            } else {
//                continuation.resumeWithException(Exception("HTTP ${response.code()} ${response.message()}"))
//            }
//        }
//
//        override fun onFailure(call: Call<T>, t: Throwable) {
//            continuation.resumeWithException(t)
//        }
//    })
//}

//suspend fun <T> Call<T>.awaitResponse(): T = suspendCancellableCoroutine {
//
//}