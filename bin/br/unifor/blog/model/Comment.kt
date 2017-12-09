package com.unifor.blog.model

import com.google.gson.annotations.Expose
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.text.SimpleDateFormat
import java.util.Date

@DatabaseTable(tableName = "comments")
class Comment:Entity {

    @Expose
    @DatabaseField(generatedId = true)
    override var id: Long = 0

    @Expose
    @DatabaseField(columnName = "author", canBeNull = false)
    var author: String = ""

    @Expose
    @DatabaseField(columnName = "date", canBeNull = false)
    var date: String = setDate()

    @Expose
    @DatabaseField(columnName = "comment", canBeNull = false)
    var comment: String = ""

    @DatabaseField(canBeNull = false, foreign = true)
    private var post: Post? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || Comment::class != o::class) {
            return false
        }

        val comment1 = o as Comment?

        if (if (author != null) author != comment1!!.author else comment1!!.author != null) {
            return false
        }
        if (if (comment != null) comment != comment1.comment else comment1.comment != null) {
            return false
        }
        return if (date != null) date == comment1.date else comment1.date == null
    }

    override fun hashCode(): Int {
        var result = author?.hashCode() ?: 0
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }

    private fun setDate(): String {
        val date = Date()
        val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
        return format.format(date)
    }
}
