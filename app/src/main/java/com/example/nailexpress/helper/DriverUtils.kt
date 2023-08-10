package com.example.nailexpress.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import com.example.nailexpress.R
import com.example.nailexpress.views.dialog.ConfirmNoticeDialog


object DriverUtils {

    private fun openIntent(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            exception.printStackTrace()
        }
    }

    private fun uri(uri: String): Uri {
        return Uri.parse(uri)
    }

    private fun intent(uri: Uri): Intent {
        return Intent(Intent.ACTION_VIEW, uri)
    }

    fun navigateMyLocationWithGoogleMap(context: Context, latitude: Float, longitude: Float) {
        val mapIntent = intent(uri("google.navigation:q=$latitude,$longitude"))
        mapIntent.setPackage("com.google.android.apps.maps")
        openIntent(context, mapIntent)
    }

    @SuppressLint("MissingPermission")
    fun call(context: Context, phoneNumber: String) {
        val confirmDialog = ConfirmNoticeDialog(context)
        confirmDialog.showCallPhone(
            context.getString(R.string.title_confirm_call),
            context.getString(R.string.msg_sure_call_phone, phoneNumber)
        ) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                context.startActivity(intent)
            }
        }
    }

    fun message(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:$phoneNumber")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}


