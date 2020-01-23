package com.example.timelineapi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleViewAdapter(private val list: MutableList<Post>) : RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.avatar.setImageResource(R.drawable.profile)
        holder.tvName.text = list[position].userName
        holder.tvText.text = list[position].text
        holder.postedAt.text = list[position].postedAt
    }
}