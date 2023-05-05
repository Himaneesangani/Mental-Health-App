package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArticleAdapter (private val context: Context, private val articleList: MutableList<Articledata>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {


    private val database = FirebaseDatabase.getInstance().reference.child("articles")

    var listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            articleList.clear()
            for (therapistSnapshot in snapshot.children) {
                var therapist = therapistSnapshot.getValue(Articledata::class.java)
                therapist?.let {
                    articleList.add(it)
                }
            }
            notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    }

    init {
        database.addValueEventListener(listener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return articleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]
        holder.title.text = article.title
        holder.description.text = article.description
        holder.title.setOnClickListener {
            val articleUrl = article.articleUrl
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
            holder.itemView.context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.articletitle)
        val description: TextView = itemView.findViewById(R.id.articledescription)
    }
}

