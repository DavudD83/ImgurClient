package com.example.network

interface ConnectivityServiceProvider {
    fun service(): ConnectionStateService
}

interface WithServiceProvider {
    fun serviceProvider(): ConnectivityServiceProvider
}