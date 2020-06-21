package com.example.network.model

data class ErrorResponse(
    val data: ErrorData,
    val status: Int
)

data class ErrorData(val error: String)