package com.unifor.blog.model

import com.google.gson.annotations.Expose
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "tags")
class Tag: Entity {

    @Expose
    @DatabaseField(generatedId = true)
    override var id: Long = 0

    @Expose
    @DatabaseField(columnName = "tag", canBeNull = false)
    var tag: String = ""

    @DatabaseField(canBeNull = false, foreign = true)
    private var post: Post? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || Tag::class != o::class) {
            return false
        }

        val tag1 = o as Tag?

        return if (this.tag != null) tag == tag1!!.tag else tag1!!.tag == null
    }

    override fun hashCode(): Int {
        return tag?.hashCode() ?: 0
    }
}
