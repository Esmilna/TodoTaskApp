package com.example.taskapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.Data.TaskModel
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
                intent.getStringExtra("taskName").toString()
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
        tvDate.text = intent.getStringExtra("date")
        tvTime.text = intent.getStringExtra("time")

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
        taskName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etTaskName = mDialogView.findViewById<EditText>(R.id.TaskName)
        val etTaskDescription = mDialogView.findViewById<EditText>(R.id.TaskDescription)
        val etDate = mDialogView.findViewById<EditText>(R.id.Date)
        val etTime = mDialogView.findViewById<EditText>(R.id.Time)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etTaskName.setText(intent.getStringExtra("taskName").toString())
        etTaskDescription.setText(intent.getStringExtra("taskDescription").toString())
        etDate.setText(intent.getStringExtra("Date").toString())
        etTime.setText(intent.getStringExtra("Time").toString())

        mDialog.setTitle("Updating $taskName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateTaskData(
                taskId,
                etTaskName.text.toString(),
                etTaskDescription.text.toString(),
                etDate.text.toString(),
                etTime.text.toString()
            )

            Toast.makeText(applicationContext, "Task Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvTaskName.text = etTaskName.text.toString()
            tvTaskDescription.text = etTaskDescription.text.toString()
            tvDate.text = etDate.text.toString()
            tvTime.text = etTime.text.toString()

            alertDialog.dismiss()
        }
    }
    fun OnClickDate(v: View?){
        val Date = findViewById<EditText>(R.id.Date)

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            selectedCalendar.set(y,m,d)
            Date.setText("$y-$m-$d")
        }
        val datePickerDialog = DatePickerDialog(this, listener, year, month, dayOfMonth)
        datePickerDialog.setOnDismissListener{

        }
        datePickerDialog.show()
    }
    fun OnClickTime(v: View?){
        val Time = findViewById<EditText>(R.id.Time)

        val hour = selectedCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = selectedCalendar.get(Calendar.MINUTE)

        val listener = TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
            selectedCalendar.set(Calendar.HOUR_OF_DAY, h)
            selectedCalendar.set(Calendar.MINUTE, m)
            Time.setText("$h:$m")

        }

        val timePickerDialog = TimePickerDialog(this, listener, hour, minute, true)
        timePickerDialog.setOnDismissListener {

        }

        timePickerDialog.show()
    }


    private fun updateTaskData(
        id: String,
        name: String,
        description: String,
        date: String,
        time: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Tasks").child(id)
        val taskInfo = TaskModel(id, name, description , date, time)
        dbRef.setValue(taskInfo)
    }


}