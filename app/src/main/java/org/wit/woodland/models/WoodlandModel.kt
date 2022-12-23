package org.wit.woodland.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
//The kotlin-parcelize plugin provides a Parcelable implementation generator.
//The Android’s Parcelable is an interface on which, the values can be written and read from a Parcel.
// The implementation of the Parcelable interface allows an object to be passed between the different Android components like, activity and fragment.
@Parcelize @Entity @TypeConverters(Converters:: class)
data class WoodlandModel(@PrimaryKey(autoGenerate = true) var id: Long=0,
                         var rb_conifer: Boolean= false,
                         var rb_broadleaf: Boolean= false,
                         var items: Boolean= false,
                         var rb_mixed: Boolean= false,
                         var fbId: String = "",
                         var title: String = "",
                         var description: String = "",
                         var images: ArrayList<String> = ArrayList<String>(),
                         var visited: Boolean= false,
                         var carpark: Boolean= false,
                         var shop: Boolean= false,
                         var toilets: Boolean= false,
                         var favourite: Boolean= false,
                         var date: String = "",
                         var notes: String ="",
                         var rating: Float = 0f,
                         @Embedded var location : Location = Location()): Parcelable
//location requires the latitude longitude and zoom
@Parcelize
data class Location(var lat: Double = 0.0,var lng: Double = 0.0,var zoom: Float = 0f) : Parcelable

