package org.wit.woodland.models
//make sure to update as it progresses
interface WoodlandStore
{
    fun findAll(): List<WoodlandModel>
    fun create(woodland: WoodlandModel)
    fun update(woodland: WoodlandModel)
    fun delete(woodland: WoodlandModel)
    fun countVisited(): Int
    fun countConifer(): Int
    fun countMixed(): Int
    fun countBroadleaf(): Int
    fun findByFbId(id:String) : WoodlandModel?
    fun clear()
    fun setFavourite(woodland: WoodlandModel)
    fun findFavourites(): List<WoodlandModel>
    fun findSearchResults(query: String): List<WoodlandModel>
}