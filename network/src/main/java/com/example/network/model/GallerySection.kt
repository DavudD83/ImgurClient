package com.example.network.model

enum class GallerySection(val value: String) {
    HOT("hot"), TOP("top"), USER("user");

    override fun toString(): String {
        return value
    }
}