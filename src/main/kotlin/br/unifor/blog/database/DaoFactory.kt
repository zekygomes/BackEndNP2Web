package com.unifor.blog.database

import com.unifor.blog.model.Entity
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import java.lang.NullPointerException

object DaoFactory {

    var connSource:JdbcConnectionSource? = null

    inline fun <reified T:Entity,ID:Number> getDaoInstance():Dao<T, ID>{
        if(connSource == null) throw NullPointerException("The JdbcConnectionSource must be initialized.")
        return DaoManager.createDao(connSource, T::class.java) as Dao<T, ID>
    }

    inline fun <reified T:Entity> createTable(): Int {
        if(connSource == null) throw NullPointerException("The JdbcConnectionSource must be initialized.")
        return TableUtils.createTable(connSource, T::class.java)
    }

}