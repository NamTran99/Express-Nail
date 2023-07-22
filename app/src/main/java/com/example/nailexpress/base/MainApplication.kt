package com.example.nailexpress.base

import android.app.Application
import android.util.Log
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this

        // Initialize the SDK
        Places.initialize(applicationContext, "AIzaSyCaw5Usj4gJ4Z9d2yhqRtd8hTfsRw76s3s")

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
    }
    companion object{
        lateinit var application: Application
    }
}