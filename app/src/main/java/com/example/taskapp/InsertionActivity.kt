package com.example.taskapp

import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.Data.TaskModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class InsertionActivity: AppCompatActivity() {

    val selectedCalendar = Calendar.getInstance()
    private lateinit var etTaskName: EditText
    private lateinit var etTaskDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etTaskName = findViewById(R.id.etTaskName)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Tasks")

        btnSaveData.setOnClickListener {
            saveTaskData()
        }


    }

    private fun saveTaskData() {

        //getting values
        val taskName = etTaskName.text.toString()
        val taskDescription = etTaskDescription.text.toString()
        val taskDate = etDate.text.toString()
        val taskTime = etTime.text.toString()

        if (taskName.isEmpty()) {
            etTaskName.error = "Please enter name"
        }
        if (taskDescription.isEmpty()) {
            etTaskDescription.error = "Please enter description"
        }
        if (taskDate.isEmpty()) {
            etDate.error = "Please enter date"
        }
        if (taskTime.isEmpty()) {
            etTime.error = "Please enter time"
        }

        val taskId = dbRef.push().key!!

        val task = TaskModel(taskId, taskName, taskDescription, taskDate, taskTime, completed = false)

        dbRef.child(taskId).setValue(task)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etTaskName.text.clear()
                etTaskDescription.text.clear()
                etDate.text.clear()
                etTime.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
    fun OnClickDate(v: View?){
        val Date = findViewById<EditText>(R.id.etDate)

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
        val Time = findViewById<EditText>(R.id.etTime)

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

}