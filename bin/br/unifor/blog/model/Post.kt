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
    private var author: User? = null

    @Expose
    @DatabaseField(columnName = "title", canBeNull = false)
    var title: String = ""

    @Expose
    @DatabaseField(columnName = "blogPost", canBeNull = false)
    var blogPost: String = ""

    @Expose
    @DatabaseField(columnName = "date", canBeNull = false)
    var date: String = setDate()


    @ForeignCollectionField(eager = false)
    var commentList: ForeignCollection<Comment>? = null

    @ForeignCollectionField(eager = false)
    var tagSet: ForeignCollection<Tag>? = null


    fun editEntry(title: String, author: User, blogPost: String) {
        date = setDate()
        this.title = title
        this.author = author
        this.blogPost = blogPost
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
        return if (blogPost != null) blogPost == blogEntry.blogPost else blogEntry.blogPost == null
    }

    override fun hashCode(): Int {
        var result = if (title != null) title!!.hashCode() else 0
        result = 31 * result + if (author != null) author!!.hashCode() else 0
        result = 31 * result + if (blogPost != null) blogPost!!.hashCode() else 0
        return result
    }

    private fun setDate(): String {
        val dateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
        val date = Date()
        return dateFormat.format(date)
    }
}
