package org.wit.woodland.models.room

import android.content.Context
import androidx.room.Room
import org.wit.woodland.models.WoodlandModel
import org.wit.woodland.models.WoodlandStore
import org.wit.woodland.room.Database
import org.wit.woodland.room.WoodlandDao
//Data Access Objects (DAO) are classes where you define your database interactions.
class WoodlandStoreRoom(val context: Context) : WoodlandStore
{
    var dao: WoodlandDao

    init
    {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.woodlandDao()
    }

    override fun findAll(): List<WoodlandModel>
    {
        return dao.findAll()
    }

    override fun setFavourite(woodland: WoodlandModel)
    {
        dao.setFavourite(woodland)
    }

    override fun findByFbId(id: String): WoodlandModel
    {
        return dao.findById(id)
    }

    override fun findSearchResults(query: String): List<WoodlandModel>
    {
        return dao.findSearchResults(query)
    }

    override fun findFavourites(): List<WoodlandModel>
    {
        return dao.findFavourites()
    }

    override fun clear()
    {}

    override fun create(woodland: WoodlandModel)
    {
        dao.create(woodland)
    }

    override fun update(woodland: WoodlandModel)
    {
        dao.update(woodland)
    }

    override fun delete(woodland: WoodlandModel)
    {
        dao.deleteWoodland(woodland)
    }

    override fun countVisited(): Int
    {
        var count = 0
        for(woodland in this.findAll()){
            if(woodland.visited){
                count ++
            }
        }
        return count
    }

    override fun countConifer(): Int
    {
        var count = 0
        for (woodland in this.findAll()) {
            if (woodland.rb_conifer) {
                count++
            }
        }
        return count
    }

    override fun countMixed(): Int
    {
        var count = 0
        for (woodland in this.findAll()) {
            if (woodland.rb_mixed) {
                count++
            }
        }
        return count
    }

    override fun countBroadleaf(): Int
    {
        var count = 0
        for (woodland in this.findAll()) {
            if (woodland.rb_broadleaf) {
                count++
            }
        }
        return count
    }
}