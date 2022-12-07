package org.wit.woodland.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.woodland.helpers.*
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.models.WoodlandStore
import java.util.*

val JSON_FILE = "woodlands.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<WoodlandModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class WoodlandJSONStore : WoodlandStore, AnkoLogger {

    val context: Context
    var woodlands = mutableListOf<WoodlandModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<WoodlandModel> {
        return woodlands
    }


    override fun countVisited(): Int {
        var count = 0
        for(woodland in woodlands){
            if(woodland.visited ==true){
                count ++
            }
        }
        return count
    }

    override fun create(woodland: WoodlandModel) {
        woodland.id = generateRandomId()
        woodlands.add(woodland)
        serialize()
    }

    override fun update(woodland: WoodlandModel) {
        val woodlandsList = findAll() as ArrayList<WoodlandModel>
        var foundWoodland: WoodlandModel? = woodlandsList.find { p -> p.id == woodland.id }
        if (foundWoodland != null) {
            foundWoodland.title = woodland.title
            foundWoodland.description = woodland.description
            foundWoodland.images = woodland.images
            foundWoodland.location = woodland.location
            foundWoodland.date = woodland.date
            foundWoodland.visited = woodland.visited
            foundWoodland.notes = woodland.notes
        }
        serialize()
    }



    override fun findByFbId(id:String) : WoodlandModel? {
        val foundWoodland: WoodlandModel? = woodlands.find { it.fbId == id }
        return foundWoodland
    }

    override fun setFavourite(woodland: WoodlandModel) {
        var foundWoodland: WoodlandModel? = woodlands.find { p -> p.fbId == woodland.fbId }
        if (foundWoodland != null) {
            foundWoodland.favourite = woodland.favourite
        }
    }

    override fun findSearchResults(query: String): List<WoodlandModel> {
        val results = woodlands.filter{p-> p.title.contains(query)}
        return results
    }

    override fun findFavourites(): List<WoodlandModel> {
        val favourites = woodlands.filter{p-> p.favourite == true}
        return favourites
    }

    override fun clear() {
        woodlands.clear()
    }

    fun logAll() {
        woodlands.forEach{ info("${it}") }
    }

    override fun delete(woodland: WoodlandModel) {
        val foundWoodland: WoodlandModel? = woodlands.find{it.id == woodland.id}
        woodlands.remove(foundWoodland)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(woodlands, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        woodlands = Gson().fromJson(jsonString, listType)
    }
}