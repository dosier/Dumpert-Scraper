package com.stan.scraper.parse.comment

import com.google.gson.annotations.Expose

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

    @Expose private val comments = ArrayList<Comment>()

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
                  @Expose val kudos : Int)

    companion object {

        const val BASE_URL = "https://comments.dumpert.nl/"
        const val BASE_PATH = "comments"

    }
}