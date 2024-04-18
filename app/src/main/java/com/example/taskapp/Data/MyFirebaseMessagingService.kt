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
            // Verificar si la notificación es para una tarea próxima a vencer
            val taskDate = remoteMessage.data["taskDate"]
            if (taskDate != null && isTaskDueSoon(taskDate)) {
                // Mostrar la notificación si la tarea está próxima a vencer
                showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
            }
        }
    }

    private fun isTaskDueSoon(taskDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(taskDate)
        val currentDate = Calendar.getInstance().time
        val difference = date.time - currentDate.time
        val daysDifference = difference / (1000 * 60 * 60 * 24)
        return daysDifference <= 1 // Notificar si está a menos de un día de vencer
    }

    private fun showNotification(title: String?, message: String?) {
        val channelId = "task_notification_channel"
        val notificationId = 1
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Task Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.tasknotification)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}
