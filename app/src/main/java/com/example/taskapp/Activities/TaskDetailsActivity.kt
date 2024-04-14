package com.example.taskapp.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.Data.TaskModel
import com.example.taskapp.R
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class TaskDetailsActivity: AppCompatActivity() {
    val selectedCalendar = Calendar.getInstance()
    private lateinit var tvtaskId: TextView
    private lateinit var tvTaskName: TextView
    private lateinit var tvTaskDescription: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("taskId").toString(),
                intent.getStringExtra("taskName").toString(),
                intent.getStringExtra("taskDescription").toString(),
                intent.getStringExtra("taskDate").toString(),
                intent.getStringExtra("taskTime").toString()

            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("taskId").toString()
            )
        }


    }

    private fun initView() {
        tvtaskId = findViewById(R.id.tvTaskId)
        tvTaskName = findViewById(R.id.tvTaskName)
        tvTaskDescription = findViewById(R.id.tvTaskDescription)
        tvDate = findViewById(R.id.tvDate)
        tvTime = findViewById(R.id.tvTime)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvtaskId.text = intent.getStringExtra("taskId")
        tvTaskName.text = intent.getStringExtra("taskName")
        tvTaskDescription.text = intent.getStringExtra("taskDescription")
        tvDate.text = intent.getStringExtra("taskDate")
        tvTime.text = intent.getStringExtra("taskTime")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Tasks").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Task data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun openUpdateDialog(
        taskId: String,
        taskName: String,
        taskDescription: String,
        date: String,
        time: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etTaskName = mDialogView.findViewById<EditText>(R.id.TaskName)
        val etTaskDescription = mDialogView.findViewById<EditText>(R.id.TaskDescription)
        val etDate = mDialogView.findViewById<EditText>(R.id.Date)
        val etTime = mDialogView.findViewById<EditText>(R.id.Time)

        etTaskName.setText(taskName)
        etTaskDescription.setText(taskDescription)
        etDate.setText(date)
        etTime.setText(time)

        mDialog.setTitle("Updating $taskName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        etDate.setOnClickListener {
            OnClickDate(etDate)
        }

        etTime.setOnClickListener {
            OnClickTime(etTime)
        }

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)
        btnUpdateData.setOnClickListener {
            updateTaskData(
                taskId,
                etTaskName.text.toString(),
                etTaskDescription.text.toString(),
                etDate.text.toString(),
                etTime.text.toString()
            )

            Toast.makeText(applicationContext, "Task Data Updated", Toast.LENGTH_LONG).show()

            // Opcionalmente, puedes cerrar el diálogo después de actualizar los datos
            alertDialog.dismiss()
        }
    }



    fun OnClickDate(etDate: EditText){


        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            selectedCalendar.set(y,m,d)
            val formattedDate = "$y-$m-$d"
            etDate.setText(formattedDate)
        }
        DatePickerDialog(this, listener, year, month, dayOfMonth).show()
    }

    fun OnClickTime(etTime: EditText){


        val hour = selectedCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = selectedCalendar.get(Calendar.MINUTE)

        val listener = TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
            selectedCalendar.set(Calendar.HOUR_OF_DAY, h)
            selectedCalendar.set(Calendar.MINUTE, m)
            val formattedTime = String.format("%02d:%02d", h, m)
            etTime.setText(formattedTime)
        }
        TimePickerDialog(this, listener, hour, minute, true).show()
    }



    private fun updateTaskData(
        id: String,
        name: String,
        description: String,
        date: String,
        time: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Tasks").child(id)
        val taskInfo = TaskModel(id, name, description , date, time, completed = false)
        dbRef.setValue(taskInfo)
    }


}