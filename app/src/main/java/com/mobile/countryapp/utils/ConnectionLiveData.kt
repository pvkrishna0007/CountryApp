package com.mobile.countryapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ConnectionLiveData @Inject constructor(@ActivityContext private val context: Context) :
    MutableLiveData<Boolean>() {

    companion object {
        var mNetworkStatusDisabledForTesting: Boolean = false
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!mNetworkStatusDisabledForTesting) {
                postValue(context.isConnected)
            }
        }
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onInactive() {
        super.onInactive()
        try {
            context.unregisterReceiver(networkReceiver)
        } catch (e: Exception) {
        }
    }
}


val Context.isConnected: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected == true