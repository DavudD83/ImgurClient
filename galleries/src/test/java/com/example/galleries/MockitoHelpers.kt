package com.example.galleries

import org.mockito.Mockito

inline fun <reified T> anyNonNull(): T = Mockito.any<T>(T::class.java)

inline fun <reified T> notNullArgThat(noinline matcher: (T) -> Boolean): T = Mockito.argThat(matcher)

inline fun <reified T> notNullEq(item: T): T = Mockito.eq(item)