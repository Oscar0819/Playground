package com.milet0819.notificationtest.common.repo

import com.milet0819.notificationtest.common.ApiResult
import com.milet0819.notificationtest.common.ApiService
import com.milet0819.notificationtest.common.dto.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

import kotlin.Exception


class PostRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPost(): Flow<ApiResult<Post>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = apiService.getPosts()

            if (response.isSuccessful) {
                val post = response.body() ?: Post()

                emit(ApiResult.Success(post))
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            Firebase.crashlytics.recordException(e)
            emit(ApiResult.Error(e)) // 기타 에러 방출
        }
    }

    suspend fun getPost2(): ApiResult<Post> {
        try {
            val response = apiService.getPosts()

            if (response.isSuccessful) {
                val post = response.body() ?: Post()

                return ApiResult.Success(post)
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            return ApiResult.Error(e)
        }
    }
}