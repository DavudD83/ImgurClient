package com.example.network.model

enum class GalleryWindow(val value: String) {
    DAY("day"), WEEK("week"), MONTH("month"), YEAR("year");

    override fun toString(): String {
        return value
    }
}