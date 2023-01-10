package com.example.network

import retrofit2.Retrofit

interface RetrofitProvider {
    fun provideRetrofit(): Retrofit
}

interface WithRetrofitProvider {
    fun retrofitProvider(): RetrofitProvider
}