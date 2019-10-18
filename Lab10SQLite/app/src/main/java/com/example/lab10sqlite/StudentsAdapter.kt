package com.example.lab10sqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.std_item_layout.view.*
import java.security.AccessControlContext

class StudentsAdapter (val item : List<Student>, val context: Context): RecyclerView.Adapter<StudentsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view_item = LayoutInflater.from(parent.context).inflate(R.layout.std_item_layout, parent, false)
        return ViewHolder(view_item)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvID?.text = item[position].id
        holder.tvName?.text = item[position].name
        holder.tvAge?.text = item[position].age.toString()
    }
    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val tvID = view.tv_id
        val tvName = view.tv_name
        val tvAge = view.tv_age
    }


}

