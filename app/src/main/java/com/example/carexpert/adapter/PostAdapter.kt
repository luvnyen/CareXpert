package com.example.carexpert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.model.Post

class PostAdapter (
    private val listPost:ArrayList<Post>
) :RecyclerView.Adapter<PostAdapter.ListViewHolder>()
{
    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data : Post)
    }

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var _username : TextView = itemView.findViewById(R.id.username)
        var _date : TextView = itemView.findViewById(R.id.date)
        var _time : TextView = itemView.findViewById(R.id.time)
        var _kota : TextView = itemView.findViewById(R.id.spinner_kota)
        var _pronvisi : TextView = itemView.findViewById(R.id.spinner_provinsi)
        var _judul : TextView = itemView.findViewById(R.id.title_post)
        var _post : TextView = itemView.findViewById(R.id.post)
        var _commentIcon : ImageView = itemView.findViewById(R.id.commentIcon)
        var _commentText : TextView = itemView.findViewById(R.id.tvCommentText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_post_field,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val post = listPost[position]

        holder._username.text = post.username
        holder._date.text = post.date
        holder._time.text = post.time
        holder._kota.text = post.kota
        holder._pronvisi.text = post.provinsi
        holder._judul.text = post.title
        holder._post.text = post.post

        holder._commentIcon.setOnClickListener {
            onItemClickCallback.onItemClicked(listPost[position])
        }

        holder._commentText.setOnClickListener {
            onItemClickCallback.onItemClicked(listPost[position])
        }
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}