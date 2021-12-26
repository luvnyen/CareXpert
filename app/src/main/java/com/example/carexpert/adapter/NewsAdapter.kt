package com.example.carexpert.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.model.News

class NewsAdapter (
    private val listPost:ArrayList<News>
) :RecyclerView.Adapter<NewsAdapter.ListViewHolder>()
{
    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data : News)
    }

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var _judul : TextView = itemView.findViewById(R.id.judul)
        var _isi : TextView = itemView.findViewById(R.id.isi)
        var _link : TextView = itemView.findViewById(R.id.link)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_news_field,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val post = listPost[position]

        holder._judul.text = post.judul
        holder._isi.text = post.isi
        holder._link.text = post.link
        holder._link.setOnClickListener {
            holder._link.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}


