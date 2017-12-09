package com.unifor.blog.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

@DatabaseTable(tableName = "tokens")
class Token:Entity{

    @DatabaseField(generatedId = true)
    override var id:Long = 0

    @DatabaseField(columnName = "key", canBeNull = false)
    var key:String = ""

    @DatabaseField(columnName = "user_id", canBeNull = false, foreign = true)
    var user:User? = null

    @DatabaseField(columnName = "created_at", canBeNull = false)
    var createdAt:Date? = null

    @DatabaseField(columnName = "validity", canBeNull = false)
    var validity:Int = 0

    @DatabaseField(columnName = "active", canBeNull = false)
    var active:Boolean = false

}