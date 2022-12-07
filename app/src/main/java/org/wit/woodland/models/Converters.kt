package org.wit.woodland.models

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun listToJson(value: ArrayList<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): ArrayList<String> {
        val array = Gson().fromJson(value, Array<String>::class.java)
        val arrayList = ArrayList(array.toMutableList())
        return arrayList
    }
}