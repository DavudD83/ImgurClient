package com.example.network.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RemoteGallery(
    val id: String,
    val title: String,
    val images: List<RemoteImage>?,
    val views: Int,
    val ups: Int,
    val downs: Int,
    val commentCount: Int
)

data class RemoteImage(
    val id: String,
    @JsonProperty("link")
    val url: String,
    val description: String?
)