package com.example.newsapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class NewsListAdapter( private val listener: NewsItemClicked): RecyclerView.Adapter<NewsViewHolder>() {
    private val items: ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news , parent,false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.bindingAdapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
    return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleview.text = currentItem.title
        holder.descriptionview.text = currentItem.description
        holder.titleview.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(currentItem.url))
            holder.itemView.context.startActivity(intent)

        }

    }

    fun updateNews(updateNews: ArrayList<News>){
        items.clear()
        items.addAll(updateNews)
        notifyDataSetChanged()
    }

}
class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val titleview: TextView = itemView.findViewById(R.id.textView)
    val descriptionview: TextView = itemView.findViewById(R.id.textView2)
}

interface NewsItemClicked{

    fun onItemClicked(items: News)
}