package com.unifor.blog.model

import com.google.gson.annotations.Expose
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

import java.text.SimpleDateFormat
import java.util.Date

@DatabaseTable(tableName = "posts")
class Post :Entity  {

    @Expose
    @DatabaseField(generatedId = true)
    override var id: Long = 0

    @Expose
    @DatabaseField(columnName = "user_id", canBeNull = false,  foreign = true)
    var author: User? = null

    @Expose
    @DatabaseField(columnName = "title", canBeNull = false)
    var title: String = ""

    @Expose
    @DatabaseField(columnName = "content", canBeNull = false)
    var content: String = ""

    @Expose
    @DatabaseField(columnName = "createdTime", canBeNull = false)
    var createdTime: String = setDate()

    @Expose
    @DatabaseField(columnName = "viewCount", canBeNull = false)
    var viewCount: String = "0"

    @ForeignCollectionField(eager = false)
    var commentList: ForeignCollection<Comment>? = null

    @Expose
    @DatabaseField(columnName = "tags", canBeNull = true)
    var tags: String = ""


    fun editEntry(title: String, author: User, blogPost: String) {
        createdTime = setDate()
        this.title = title
        this.author = author
        this.content = blogPost
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || Post::class != o::class) {
            return false
        }

        val blogEntry = o as Post?

        if (if (title != null) title != blogEntry!!.title else blogEntry!!.title != null) {
            return false
        }
        if (if (author != null) author != blogEntry.author else blogEntry.author != null) {
            return false
        }
        return if (content != null) content == blogEntry.content else blogEntry.content == null
    }

    override fun hashCode(): Int {
        var result = if (title != null) title!!.hashCode() else 0
        result = 31 * result + if (author != null) author!!.hashCode() else 0
        result = 31 * result + if (content != null) content!!.hashCode() else 0
        return result
    }

    private fun setDate(): String {
        val dateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
        val date = Date()
        return dateFormat.format(date)
    }
}
