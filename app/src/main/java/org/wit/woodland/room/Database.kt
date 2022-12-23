package org.wit.woodland.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.wit.woodland.models.WoodlandModel


@Database(entities = arrayOf(WoodlandModel::class), version = 1,  exportSchema = false)

abstract class Database : RoomDatabase()
{
    abstract fun woodlandDao(): WoodlandDao
}