package com.unifor.blog.model

import com.google.gson.annotations.Expose
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable


@DatabaseTable(tableName = "users")
class User:Entity {

    @Expose
    @DatabaseField(generatedId = true)
    override var id: Long = 0

    @Expose
    @DatabaseField(columnName = "name", canBeNull = false)
    var name: String = ""

    @Expose
    @DatabaseField(columnName = "email", canBeNull = false)
    var email: String = ""

    @Expose
    @DatabaseField(columnName = "password", canBeNull = false)
    var password: String = ""

    @Expose
    @DatabaseField(columnName = "salt", canBeNull = false)
    var salt: String = ""

    @ForeignCollectionField( eager = false)
    var tokens: ForeignCollection<Token>? = null


}