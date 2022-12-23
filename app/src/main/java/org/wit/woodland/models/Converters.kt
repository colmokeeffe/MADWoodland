package org.wit.woodland.models

import androidx.room.TypeConverter
import com.google.gson.Gson
//converters are used in the conversion process in which one data type variable is converted into another data type.
//we require both functions to convert
//Gson is a Java library that can be used to convert Java objects into their JSON representation.
//can also be used to convert a JSON string to an equivalen
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