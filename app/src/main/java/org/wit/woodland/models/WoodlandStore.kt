package org.wit.woodland.models

interface WoodlandStore {
    fun findAll(): List<WoodlandModel>
    fun create(woodland: WoodlandModel)
    fun update(woodland: WoodlandModel)
    fun delete(woodland: WoodlandModel)
}