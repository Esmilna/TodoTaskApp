package com.example.taskapp.Data

data class TaskModel(
    var taskId: String? = null,
    var taskName: String? = null,
    var taskDescription: String? = null,
    var taskDate: String? = null,
    var taskTime: String? = null,
    var completed: Boolean = false,

)



