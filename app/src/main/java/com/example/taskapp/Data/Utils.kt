package com.example.taskapp.Data

object Utils {
    fun sortTasksByDueDate(taskList: ArrayList<TaskModel>): ArrayList<TaskModel> {
        return taskList.sortedBy { it.taskDate }.toCollection(ArrayList())
    }
}