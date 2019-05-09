package com.stan.scraper.page.comment

import com.google.gson.annotations.Expose

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
class CommentsCache {

    @Expose lateinit var topComment : Comment
    @Expose private val comments = ArrayList<Comment>()

    fun add(comment: Comment){
        comments.add(comment)
    }

    fun sortByUser(){
        comments.sortByDescending { it.user }
    }

    fun sortByContent(){
        comments.sortByDescending { it.content }
    }

    fun sortByKudos(){
        comments.sortByDescending { it.kudos }
    }

    fun sortByTime(){
        comments.sortByDescending { it.dateTime }
    }

    class Comment(@Expose val user: String, @Expose val content : String, @Expose val dateTime : String, @Expose val kudos : Int)

}