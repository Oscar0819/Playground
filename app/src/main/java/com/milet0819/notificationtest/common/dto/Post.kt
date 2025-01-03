package com.milet0819.notificationtest.common.dto

class Post : ArrayList<PostItem>()

data class PostItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)