package com.example.taskapp.Data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.taskapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.data.isNotEmpty().let {
            val taskDate = remoteMessage.data["taskDate"]
            if (taskDate != null && isTaskDueSoon(taskDate)) {
                showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
            }
        }
    }

    private fun isTaskDueSoon(taskDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val taskDueDate = sdf.parse(taskDate)
        val difference = taskDueDate.time - currentDate.time
        val daysDifference = difference / (1000 * 60 * 60 * 24)
        return daysDifference <= 1 // Notificar si está a menos de un día de vencer
    }

    private fun showNotification(title: String?, message: String?) {
        val channelId = "TaskNotificationChannel"
        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.tasknotification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Task Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
