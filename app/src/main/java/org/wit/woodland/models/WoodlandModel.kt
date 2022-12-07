package org.wit.woodland.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@TypeConverters(Converters:: class)
data class WoodlandModel(@PrimaryKey(autoGenerate = true) var id: Long=0,
                         var fbId: String = "",
                         var title: String = "",
                         var description: String = "",
                         var images: ArrayList<String> = ArrayList<String>(),
                         var visited: Boolean= false,
                         var favourite: Boolean= false,
                         var date: String = "",
                         var notes: String ="",
                         var rating: Float = 0f,
                         @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable