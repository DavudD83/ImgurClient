package com.example.base_galleries

import java.util.*

data class Gallery(
    val id: String,
    val title: String,
    val views: Int,
    val upVotes: Int,
    val downVotes: Int,
    val commentsCount: Int,
    val images: List<Image>,
    val date: Date
)