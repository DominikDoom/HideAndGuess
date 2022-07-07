package de.thm.mobiletech.hideandguess

import android.media.Image
import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable{

    lateinit var username: String
    lateinit var password: String
    var isIngame: Boolean = false
    lateinit var image : Image

    constructor(parcel: Parcel) : this() {
        username = parcel.readString()!!
        password = parcel.readString()!!
        isIngame = parcel.readByte() != 0.toByte()
    }

    constructor(username: String, password: String, image: Image) : this() {
        this.username = username
        this.password = password
        this.image = image
    }

    constructor(username: String) : this() {
        this.username = username
1    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeByte(if (isIngame) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}