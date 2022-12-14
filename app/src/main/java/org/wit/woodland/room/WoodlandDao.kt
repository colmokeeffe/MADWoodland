package org.wit.woodland.room

import androidx.room.*
import org.wit.woodland.models.WoodlandModel
//Data Access Objects (DAO) are classes where you define your database interactions.
//The class marked with @Dao should either is an interface in this case,
// At compile time, this room will generate an implementation of this class when it is referenced by the Database
@Dao interface WoodlandDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(woodland: WoodlandModel)

    @Query("SELECT * FROM WoodlandModel")
    fun findAll(): List<WoodlandModel>

    @Query("select * from WoodlandModel where id = :id")
    fun findById(id: String): WoodlandModel

    @Update
    fun update(woodland: WoodlandModel)

    @Delete
    fun deleteWoodland(woodland: WoodlandModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavourite(woodland: WoodlandModel)

    @Query("select * from WoodlandModel where favourite = 1")
    fun findFavourites(): List<WoodlandModel>

    @Query("select * from WoodlandModel where title like :query")
    fun findSearchResults(query: String): List<WoodlandModel>
}