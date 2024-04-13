package com.example.taskapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.Data.TaskModel
import com.example.taskapp.R
import com.google.firebase.database.FirebaseDatabase

class TaskAdapter (private val taskList: ArrayList<TaskModel>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return ViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.tvTaskName.text = currentTask.taskName

        holder.chkComplete.isChecked = currentTask.completed

        // Configurar el listener para el CheckBox de completado
        holder.chkComplete.setOnCheckedChangeListener { _, isChecked ->
            currentTask.completed = isChecked
            updateTaskCompletedStatus(currentTask)
        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvTaskName : TextView = itemView.findViewById(R.id.tvTaskName)
        val chkComplete: CheckBox = itemView.findViewById(R.id.chkComplete)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

    private fun updateTaskCompletedStatus(task: TaskModel) {
        // Actualizar el estado de la tarea en tu base de datos (Firebase, por ejemplo)
        val dbRef = FirebaseDatabase.getInstance().getReference("Tasks").child(task.taskId.toString())
        dbRef.child("completed").setValue(task.completed)
    }


}