package com.example.taskapp.Activities


import android.app.DatePickerDialog

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.taskapp.R
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCalendar = findViewById<Button>(R.id.btnCalendar)
        btnCalendar.setOnClickListener { abrirCalendario() }

        val drawable: Drawable? = resources.getDrawable(R.drawable.baseline_calendar_month_24)
        btnCalendar.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)

        btnInsertData.setOnClickListener{
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)

        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)


        }


    }
    private fun abrirCalendario() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, dayOfMonth ->
            // Aquí puedes realizar acciones con la fecha seleccionada
        }, year, month, dayOfMonth)
        dpd.show()
    }


}