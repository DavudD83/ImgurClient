package com.example.network

interface GalleriesApiProvider {
    fun galleriesApi(): GalleriesApi
}

interface WithGalleriesApiProvider {
    fun provider(): GalleriesApiProvider
}