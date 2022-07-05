package de.thm.mobiletech.hideandguess

import android.media.Image

class User {

    lateinit var username: String
    lateinit var password: String
    var isIngame: Boolean = false
    lateinit var image : Image

    constructor(username: String, password: String, image: Image) {
        this.username = username
        this.password = password
        this.image = image
    }


}