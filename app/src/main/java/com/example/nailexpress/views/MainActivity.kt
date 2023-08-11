package com.example.nailexpress.views

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.databinding.ActivityMainBinding
import com.example.nailexpress.datasource.local.PrefUtils
import com.example.nailexpress.event.EventNotification
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(layoutId = R.layout.activity_main) {
    override val fragmentContainerView = R.id.fragmentContainerView
    private val prefUtils by lazy { PrefUtils(context = this) }
    private val eventNotification = EventNotification()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
//            showPopupNotificationPermission()
        } else {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        askNotificationPermission()

        supportFragmentManager.registerFragmentLifecycleCallbacks(eventNotification, true)
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showPopupNotificationPermission() {
        // Tạo intent để mở trang cài đặt thông báo
        val intent = Intent()
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

        // Kiểm tra xem thiết bị hỗ trợ intent này hay không
        if (intent.resolveActivity(packageManager) != null) {
            // Hiển thị popup nhắc người dùng bật quyền thông báo
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài đặt thông báo")
            builder.setMessage("Vui lòng bật quyền thông báo popup cho ứng dụng")
            builder.setPositiveButton("OK") { dialog, which ->
                // Mở trang cài đặt thông báo
                startActivity(intent)
                dialog.dismiss()
            }
            builder.setCancelable(true)
            builder.show()
        }
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "0",
                "Nail Express channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Channel Description"

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onResume() {
        super.onResume()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            prefUtils.saveDeviceId(token)
        })
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(eventNotification)
        super.onDestroy()
    }
}