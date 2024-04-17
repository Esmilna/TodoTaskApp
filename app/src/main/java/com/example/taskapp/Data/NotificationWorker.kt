package com.example.taskapp.Data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class NotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Obtener las tareas próximas a vencer
            val tasks = getTasksDueSoon()

            // Programar notificaciones para las tareas próximas a vencer
            scheduleNotifications(tasks)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun getTasksDueSoon(): List<TaskModel> {
        val db = FirebaseFirestore.getInstance()
        val tasksCollection = db.collection("tasks")
        val currentDate = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return tasksCollection.whereGreaterThan("taskDate", sdf.format(currentDate)).get()
            .await()
            .toObjects(TaskModel::class.java)
            .filter { task ->
                val taskDate = sdf.parse(task.taskDate)
                val difference = taskDate.time - currentDate.time
                val daysDifference = difference / (1000 * 60 * 60 * 24)
                daysDifference <= 1 // Notificar si está a menos de un día de vencer
            }
    }

    private fun scheduleNotifications(tasks: List<TaskModel>) {
        // Lógica para programar las notificaciones para las tareas encontradas
        // Aquí puedes usar el código de programación de notificaciones que hemos discutido anteriormente
        // Por ejemplo, programar una notificación para cada tarea en la lista
    }
}
