package com.example.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import java.io.Closeable

class ConnectionStateService private constructor(private val context: Context) : Closeable {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val networkInfo = connectivityManager.activeNetworkInfo
            connectionStateProcessor.onNext(networkInfo?.isConnected ?: false)
        }
    }

    private val connectionStateProcessor = BehaviorProcessor.create<Boolean>()

    override fun close() {
        context.unregisterReceiver(receiver)
    }

    fun trackConnectionState(): Flowable<Boolean> = connectionStateProcessor.distinctUntilChanged()

    companion object {
        fun create(context: Context): ConnectionStateService {
            val service = ConnectionStateService(context)

            context.registerReceiver(service.receiver, INTENT_FILTER)

            return service
        }

        private val INTENT_FILTER = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }
}