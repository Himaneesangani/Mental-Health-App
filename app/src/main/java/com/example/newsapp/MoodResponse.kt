package com.example.newsapp
data class MoodResponse(val mood: String = "", val timestamp: Long = 0) {
    fun getMooddata(): String {
        return mood
    }
}



