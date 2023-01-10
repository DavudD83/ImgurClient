package com.example.network

import com.example.network.model.NetworkError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

class RetryWhenConnectedUseCase @Inject constructor(private val connectionStateService: ConnectionStateService) {

    suspend fun handleError(error: Throwable, action: () -> Unit) {
        if (error is NetworkError) {
            connectionStateService.trackConnectionState()
                .asFlow()
                .first { isConnected -> isConnected }
            action()
        }
    }
}