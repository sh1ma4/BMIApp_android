package com.example.bmiapp.ui.history.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bmiapp.Model.BmiModel
import com.example.bmiapp.R
import com.example.bmiapp.ui.history.recycleview.ViewHolder

class ViewAdapter(private var list: MutableList<BmiModel>, private val context: Context?) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView: View = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.dateView.text  = list[position].day
        viewHolder.heightView.text = list[position].height
        viewHolder.weightView.text = list[position].weight
        viewHolder.bmiView.text = list[position].bmi
        viewHolder.memoView.text = list[position].memo

        viewHolder.memoView.visibility = if (viewHolder.memoView.text.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refresh(newList: MutableList<BmiModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}