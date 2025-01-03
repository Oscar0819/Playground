package com.milet0819.notificationtest.common.repo

import com.milet0819.notificationtest.common.ApiResult
import com.milet0819.notificationtest.common.ApiService
import com.milet0819.notificationtest.common.dto.Post
import com.milet0819.notificationtest.common.dto.Users
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UsersRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getUsers(): Flow<ApiResult<Users>> = flow {
        emit(ApiResult.Loading)

        try {
            val response = apiService.getUsers()

            if (response.isSuccessful) {
                val users = response.body() ?: Users()

                emit(ApiResult.Success(users))
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            Firebase.crashlytics.recordException(e)
            emit(ApiResult.Error(e)) //에러 방출
        }
    }
}