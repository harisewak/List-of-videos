package com.harisewak.verticalvideos.data

import android.os.Parcel
import android.os.Parcelable

data class Video(
    val description: String,
    val videoUrl: String,
    val subtitle: String,
    val thumb: String,
    val title: String,
    var curPosition: Long = 0L,
    var isPlaying: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(videoUrl)
        parcel.writeString(subtitle)
        parcel.writeString(thumb)
        parcel.writeString(title)
        parcel.writeLong(curPosition)
        parcel.writeByte(if (isPlaying) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }

}