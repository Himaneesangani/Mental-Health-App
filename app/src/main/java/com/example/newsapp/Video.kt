package com.example.newsapp

import android.icu.text.CaseMap.Title

class Video {
    var title: String? = null
    var url: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Therapist.class)
    }
    constructor(title: String? , url: String?){
        this.title = title
        this.url = url
    }
}

