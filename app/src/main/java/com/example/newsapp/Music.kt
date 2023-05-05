package com.example.newsapp

import android.icu.text.CaseMap.Title

class Music
{
    var title: String?= null
    var url: String?= null
    constructor(){

    }
    constructor(title:String?, url: String?){
        this.title= title
        this.url = url
    }
}

