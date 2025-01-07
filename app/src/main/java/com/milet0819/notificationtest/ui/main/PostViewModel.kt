package com.milet0819.notificationtest.ui.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milet0819.notificationtest.common.ApiResult
import com.milet0819.notificationtest.common.dto.Post
import com.milet0819.notificationtest.common.dto.Users
import com.milet0819.notificationtest.common.repo.PostRepository
import com.milet0819.notificationtest.common.repo.UsersRepository
import com.milet0819.notificationtest.common.utils.logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(@ApplicationContext private val context: Context,
                                        private val postRepository: PostRepository,
                                        private val usersRepository: UsersRepository) : ViewModel() {

    private val _postState = MutableStateFlow<ApiResult<Post>>(ApiResult.Init)
    val postState: StateFlow<ApiResult<Post>> = _postState

    private val _postData = MutableLiveData<Post>()
    val postData: LiveData<Post>
        get() = _postData

    private val _usersState = MutableStateFlow<ApiResult<Users>>(ApiResult.Init)
    val usersState: StateFlow<ApiResult<Users>> = _usersState

    suspend fun fetchPost() = viewModelScope.launch {
        Log.e("postViewModel", "Started fetchPost")
        postRepository.getPost()
            .flowOn(Dispatchers.Main)
            .collect {
                _postState.value = it
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    suspend fun fetchUsers() = viewModelScope.launch {
        Log.e("postViewModel", "Started fetchUsers")
        usersRepository.getUsers()
            .flowOn(Dispatchers.Main)
            .collect {
                _usersState.value = it
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    suspend fun fetchPost2() = viewModelScope.launch {
        try {
            val result = postRepository.getPost2()

            when (result) {
                is ApiResult.Error -> {

                }
                ApiResult.Init -> {

                }
                ApiResult.Loading -> {
                    logger("Loading...")
                }
                is ApiResult.Success -> {
                    logger("Success")
                    _postData.value = result.data
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

//class PostViewModelFactory(private val postRepository: PostRepository, private val usersRepository: UsersRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
//            return PostViewModel(postRepository, usersRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}