package com.example.nailexpress.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.nailexpress.R
import com.example.nailexpress.datasource.local.PrefUtils
import com.example.nailexpress.views.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {

    private val prefUtils by lazy { PrefUtils(context = applicationContext) }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let { notification ->
            val title = notification.title
            val messageBody = notification.body
            val image = notification.imageUrl

            // Hiển thị thông báo
            showNotification(title, messageBody, image)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        prefUtils.saveDeviceId(token)
    }

    private fun showNotification(title: String?, message: String?, image: Uri?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notification_title", title)
            putExtra("notification_message", message)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "0")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(image?.port ?: R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "0",
                "Nail Express channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Channel Description"

            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1, builder.build())
    }
}