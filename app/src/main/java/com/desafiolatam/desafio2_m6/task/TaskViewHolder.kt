package com.desafiolatam.desafio2_m6.task

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.desafiolatam.desafio2_m6.R


class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val taskText = view.findViewById<TextView>(R.id.task_text)
}