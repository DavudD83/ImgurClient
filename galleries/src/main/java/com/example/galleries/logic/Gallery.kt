package com.example.galleries.logic

data class Gallery(
    val id: String,
    val title: String,
    val views: Int,
    val upVotes: Int,
    val downVotes: Int,
    val commentsCount: Int,
    val images: List<Image>
)