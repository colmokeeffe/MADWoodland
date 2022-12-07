package org.wit.woodland.models.mem

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.models.WoodlandStore

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class WoodlandMemStore : WoodlandStore, AnkoLogger {

    val woodlands = ArrayList<WoodlandModel>()

    override fun findAll(): List<WoodlandModel> {
        return woodlands
    }

    override fun create(woodland: WoodlandModel) {
        woodland.id = getId()
        woodlands.add(woodland)
        logAll();

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

    override fun delete(woodland: WoodlandModel) {
        woodlands.remove(woodland)
    }

    override fun update(woodland: WoodlandModel) {
        var foundWoodland: WoodlandModel? = woodlands.find { p -> p.id == woodland.id }
        if (foundWoodland != null) {
            foundWoodland.title = woodland.title
            foundWoodland.description = woodland.description
            foundWoodland.images = woodland.images
            foundWoodland.location = woodland.location
            logAll();
        }
    }

    fun logAll() {
        woodlands.forEach{ info("${it}") }
    }

    override fun findByFbId(id:String) : WoodlandModel? {
        val foundWoodland: WoodlandModel? = woodlands.find { it.fbId == id }
        return foundWoodland
    }
    override fun findSearchResults(query: String): List<WoodlandModel> {
        val results = woodlands.filter{p-> p.title.contains(query)}
        return results
    }

    override fun setFavourite(woodland: WoodlandModel) {
        var foundWoodland: WoodlandModel? = woodlands.find { p -> p.fbId == woodland.fbId }
        if (foundWoodland != null) {
            foundWoodland.favourite = woodland.favourite
        }
    }

    override fun findFavourites(): List<WoodlandModel> {
        val favourites = woodlands.filter{p-> p.favourite == true}
        return favourites
    }

    override fun clear() {
        woodlands.clear()
    }
}