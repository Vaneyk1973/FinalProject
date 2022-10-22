package com.example.finalproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.service.classes.Task

class TaskManagerFragment(private val inVillage: Boolean=false):Fragment(), View.OnClickListener {

    private lateinit var back:Button
    private lateinit var takeTask:Button
    private lateinit var tasks: RecyclerView
    private var chosenTask: Task? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = requireView().findViewById(R.id.task_manager_back_button)
        takeTask = requireView().findViewById(R.id.take_task_button)
        if (!inVillage) takeTask.visibility = View.GONE
        tasks = requireView().findViewById(R.id.tasks_list)
        tasks.layoutManager = LinearLayoutManager(context)
        tasks.adapter = TasksAdapter(MainActivity.tasks)
        takeTask.setOnClickListener(this)
        back.setOnClickListener(this)
    }

    private inner class TasksAdapter(private val data: ArrayList<Task>) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.task, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = data[position].name
            val description:TextView = view!!.findViewById(R.id.task_description)
            holder.name.setOnClickListener { description.text = data[position].description }
            when {
                data[position].completed -> holder.name.setBackgroundColor(Color.GREEN)
                data[position].taken -> holder.name.setBackgroundColor(Color.YELLOW)
                else -> {
                    holder.name.setOnClickListener {
                        description.text = data[position].description
                        chosenTask = data[position]
                    }
                }
            }
        }

        override fun getItemCount(): Int=data.size

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.textView15)
        }
    }

    override fun onClick(p0: View?) {
        if (p0==takeTask){
            if (chosenTask == null) Toast.makeText(context, "Take task first", Toast.LENGTH_SHORT).show()
            else {
                MainActivity.tasks[MainActivity.tasks.indexOf(chosenTask)].taken = true
                MainActivity.player.checkTasks()
                tasks.adapter = TasksAdapter(MainActivity.tasks)
            }
        } else if (p0==back){
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.tasks)!!)
            if (inVillage) {
                fragmentTransaction.add(R.id.menu, MenuFragment())
                fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
                fragmentTransaction.add(R.id.status, StatusBarFragment())
            } else fragmentTransaction.add(R.id.settings_menu, SettingsMenuFragment())
            fragmentTransaction.commit()
        }
    }
}