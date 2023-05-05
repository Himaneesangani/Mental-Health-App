package com.example.newsapp

public class Articledata {
    var title: String? = null
    var description: String? = null
    var articleUrl: String? = null


    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Therapist.class)
    }

    constructor(name: String?,description : String?, articleUrl: String?) {
        this.title = title
        this.description = description
        this.articleUrl = articleUrl
    }
    }

