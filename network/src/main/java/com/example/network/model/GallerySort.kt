package com.example.network.model

enum class GallerySort(val value: String) {
    VIRAL("viral"), TOP("top"), TIME("time");

    override fun toString(): String {
        return value
    }
}