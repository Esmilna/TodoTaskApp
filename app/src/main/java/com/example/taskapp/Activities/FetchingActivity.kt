package com.example.taskapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.Adapter.TaskAdapter
import com.example.taskapp.Data.TaskModel
import com.example.taskapp.Data.Utils.sortTasksByDueDate
import com.example.taskapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {
    private lateinit var rvTaskRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var taskList: ArrayList<TaskModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        rvTaskRecyclerView = findViewById(R.id.rvTask)
        rvTaskRecyclerView.layoutManager = LinearLayoutManager(this)
        rvTaskRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        arrayListOf<TaskModel>().also { taskList = it }

        getTasksData()

    }

    private fun getTasksData() {

        rvTaskRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Tasks")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskList.clear()
                if (snapshot.exists()){
                    for (taskSnap in snapshot.children){
                        val tasksData = taskSnap.getValue(TaskModel::class.java)
                        taskList.add(tasksData!!)g
                    }
                    taskList = sortTasksByDueDate(taskList)


                    val mAdapter = TaskAdapter(taskList)
                    rvTaskRecyclerView.adapter = mAdapter


                    mAdapter.setOnItemClickListener(object : TaskAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, TaskDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("taskId", taskList[position].taskId)
                            intent.putExtra("taskName", taskList[position].taskName)
                            intent.putExtra("taskDescription", taskList[position].taskDescription)
                            intent.putExtra("taskDate", taskList[position].taskDate)
                            intent.putExtra("taskTime", taskList[position].taskTime)
                            startActivity(intent)
                        }

                    })

                    rvTaskRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}


