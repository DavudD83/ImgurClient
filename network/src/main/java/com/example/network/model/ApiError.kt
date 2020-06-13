package com.example.network.model

data class ApiError(
    val errorMessage: String,
    val errorCode: Int
) : Throwable()