package com.example.nailexpress.datasource.local

import android.content.Context

class PrefUtils(private val context: Context) {

    fun saveDeviceId(token: String) {
        SharePrefs.getInstance(context).put(KEY_PREF_DEVICE_ID, token)
    }

    fun getDeviceId(): String? {
        return SharePrefs.getInstance(context).get<String>(KEY_PREF_DEVICE_ID)
    }
}