package org.wit.woodland.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class WoodlandMemStore : WoodlandStore {

    val woodlands = ArrayList<WoodlandModel>()

    override fun findAll(): List<WoodlandModel> {
        return woodlands
    }

    override fun create(woodland: WoodlandModel) {
        woodland.id = getId()
        woodlands.add(woodland)
        logAll()
    }

    override fun update(woodland: WoodlandModel) {
        val foundWoodland: WoodlandModel? = woodlands.find { p -> p.id == woodland.id }
        if (foundWoodland != null) {
            foundWoodland.title = woodland.title
            foundWoodland.description = woodland.description
            foundWoodland.image = woodland.image
            foundWoodland.lat = woodland.lat
            foundWoodland.lng = woodland.lng
            foundWoodland.zoom = woodland.zoom
            logAll()
        }
    }

    override fun delete(woodland: WoodlandModel) {
        woodlands.remove(woodland)
    }

    private fun logAll() {
        woodlands.forEach { i("$it") }
    }
}