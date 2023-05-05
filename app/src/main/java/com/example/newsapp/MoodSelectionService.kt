package com.example.newsapp

import android.app.IntentService
import android.content.Intent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MoodSelectionService : IntentService("MoodSelectionService") {

    private lateinit var database: DatabaseReference

    override fun onCreate() {
        super.onCreate()
        database = Firebase.database.reference
    }

    override fun onHandleIntent(intent: Intent?) {
        val mood = intent?.getStringExtra("mood")
        if (mood != null) {
            database.child("users/moods").push().setValue(mood)
        }
    }
}
