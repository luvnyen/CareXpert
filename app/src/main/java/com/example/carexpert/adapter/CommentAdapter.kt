package com.example.carexpert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.model.Comment

class CommentAdapter (
    private val listPost:ArrayList<Comment>
) :RecyclerView.Adapter<CommentAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Comment)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _username_comment: TextView = itemView.findViewById(R.id.username)
        var _comment: TextView = itemView.findViewById(R.id.comments)
        var _date_comment: TextView = itemView.findViewById(R.id.date)
        var _time_comment: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_comment_field, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val post = listPost[position]

        holder._username_comment.text = post.username_comment
        holder._comment.text = post.comment
        holder._date_comment.text = post.date_comment
        holder._time_comment.text = post.time_comment

        holder._username_comment.setOnClickListener {
            onItemClickCallback.onItemClicked(listPost[position])
        }
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}


