package com.example.newsapp

import AudioAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class RecoverFragment : Fragment() {

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var audioAdapter: AudioAdapter

    val databaseReference = FirebaseDatabase.getInstance().reference.child("music")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recover, container, false)
        val textView: TextView = view.findViewById(R.id.video)
        textView.setOnClickListener {
            val url = "https://www.healthline.com/video"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


        val articleRecyclerView: RecyclerView = view.findViewById(R.id.articlerecyclerview)
        articleRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        articleAdapter = ArticleAdapter(requireContext(), mutableListOf())
        articleRecyclerView.adapter = articleAdapter

        val musicRecyclerView: RecyclerView = view.findViewById(R.id.musicrecyclerview)
        val musicList = mutableListOf<Music>()
        val audioAdapter = AudioAdapter(musicList)
        musicRecyclerView.adapter = audioAdapter
        musicRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list before adding new items
                musicList.clear()

                // Iterate through each child in the "music" node
                for (musicSnapshot in snapshot.children) {
                    // Deserialize the child into a Music object
                    val music = musicSnapshot.getValue(Music::class.java)

                    // Add the Music object to the list
                    music?.let {
                        musicList.add(it)
                    }
                }

                // Notify the adapter that the data set has changed
                audioAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error loading audio files: ${error.message}")
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirebaseDatabase.getInstance().reference.child("articles").removeEventListener(articleAdapter.listener)
//        FirebaseDatabase.getInstance().reference.child("music").removeEventListener(audioAdapter.listener)

    }
}
