package com.mobile.countryapp

import android.app.Application
import android.util.Log
import com.mobile.countryapp.utils.ConnectionLiveData
import com.mobile.countryapp.utils.isConnected
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AlbumApplication : Application() {

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate() {
        super.onCreate()

        connectionLiveData.value = isConnected
        Log.d("AlbumApplication", "isOnline:$isConnected")
    }
}