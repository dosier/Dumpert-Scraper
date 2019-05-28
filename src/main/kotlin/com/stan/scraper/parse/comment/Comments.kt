package com.stan.scraper.parse.comment

import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import com.stan.Serializer
import com.stan.scraper.parse.dump.Dumps

/**
 * This class represents a collection of comments of one comment page.
 *
 * @see [topComment] the top comment of the page.
 * @see [comments] for all non-top comments of the page.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
class Comments(private val pageId: String) {

    @Expose lateinit var topComment : Comment

    @Expose val comments = ArrayList<Comment>()

    /**
     * Add a non-top comment to the [comments] list.
     *
     * @param comment the [Comment] to add to the list.
     */
    fun add(comment: Comment){
        comments.add(comment)
    }

    /**
     * Sorts the [comments] list by [Comment.user] in a descending order.
     */
    fun sortByUser(){
        comments.sortByDescending { it.user }
    }

    /**
     * Sorts the [comments] list by [Comment.content] in a descending order.
     */
    fun sortByContent(){
        comments.sortByDescending { it.content }
    }

    /**
     * Sorts the [comments] list by [Comment.kudos] in a descending order.
     */
    fun sortByKudos(){
        comments.sortByDescending { it.kudos }
    }

    /**
     * Sorts the [comments] list by [Comment.dateTime] in a descending order.
     */
    fun sortByTime(){
        comments.sortByDescending { it.dateTime }
    }


    override fun toString(): String {
        return pageId.replace("/", "_")
    }

    /**
     * Represents one comment on a dumper page.
     *
     * @param user      the username of the commenter.
     * @param content   the content of the comment (text).
     * @param dateTime  the time and date of when this comment was placed.
     * @param kudos     the kudos score of this comment.
     */
    class Comment(@Expose val user: String,
                  @Expose val content : String,
                  @Expose val dateTime : String,
                  @Expose val kudos : Int,
                  @Expose var subComments : List<Comment>? = null){

        override fun toString(): String {
            return "Comment(user='$user', content='$content', dateTime='$dateTime', kudos=$kudos)"
        }
    }



    companion object {

        const val BASE_URL = "https://comments.dumpert.nl/"
        const val BASE_PATH = "comments"
        private val type = object : TypeToken<Comments>(){}.type!!

        fun loadByDumps(vararg dumps: Dumps) : List<Comments> {

            val pageIds = ArrayList<String>()

            dumps.forEach { pageIds.addAll(it.getPageIds()) }

            return loadByIds(*pageIds.toTypedArray())
        }

        fun loadByIds(vararg pageIds: String) : List<Comments> {
            return pageIds.map { path(it) }
                .filter { Serializer.exists(it) }
                .map { Serializer.deserialize<Comments>(it, type) }
        }

        fun path(id : String) : String {
            return "$BASE_PATH/${id.replace("/", "_")}"
        }
    }
}