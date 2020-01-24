package com.example.timelineapi

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val avatar: ImageView = view.findViewById(R.id.avatar)
    val tvName: TextView = view.findViewById(R.id.tv_name)
    val tvText: TextView = view.findViewById(R.id.tv_text)
    val postedAt: TextView = view.findViewById(R.id.posted_at)
}