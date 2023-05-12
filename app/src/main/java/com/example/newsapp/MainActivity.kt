package com.example.newsapp


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var binding : ActivityMainBinding
    private lateinit var mAdapter: NewsListAdapter


    val user = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
         binding.floatingActionButton.setOnClickListener {
            if (user != null) {
                // User is already logged in, redirect to homescreen
                val intent = Intent(this, HomeScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // User is not logged in, show login button
                binding.floatingActionButton.setOnClickListener {
                    val intent = Intent(this, LogInActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter( this)
        binding.recyclerview.adapter = mAdapter
    }
    private fun fetchData() {
        val url = "https://gnews.io/api/v4/top-headlines?category=general&lang=en&country=in&max=100&apikey=0a03011fb33b37102ab6b25230386c6f"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title") ,
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("url")

                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)


            },
            {
                // Handle error
            }
        )
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    override fun onItemClicked(items: News) {
        }

}


