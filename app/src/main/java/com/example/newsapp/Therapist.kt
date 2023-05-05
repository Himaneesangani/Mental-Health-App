package com.example.newsapp

import android.provider.ContactsContract.CommonDataKinds.Email

class Therapist {
    var phoneNumber: Long? = null
    var name: String? = null
    var speciality: String? = null
    var about: String? = null
    var imageUrl: String? = null
    var email:String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Therapist.class)
    }

    constructor(name: String?, speciality: String?, about: String?, imageUrl: String?, phoneNumber: Long?, email: String?) {
        this.name = name
        this.speciality = speciality
        this.about = about
        this.imageUrl = imageUrl
        this.phoneNumber = phoneNumber
        this.email = email
    }
}

