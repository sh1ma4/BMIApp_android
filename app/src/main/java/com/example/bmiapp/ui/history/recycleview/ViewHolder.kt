package com.example.bmiapp.ui.history.recycleview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bmiapp.R
import android.widget.TextView

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateView: TextView = itemView.findViewById(R.id.date)
    val heightView: TextView = itemView.findViewById(R.id.height)
    val weightView: TextView = itemView.findViewById(R.id.weight)
    val bmiView: TextView = itemView.findViewById(R.id.bmi)
    val memoView: TextView = itemView.findViewById(R.id.memo)
}