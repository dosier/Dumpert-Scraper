package com.stan.evaluation

import com.stan.scraper.parse.comment.Comments

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-23
 * @version 1.0
 */
class UserEvaluation(var kudoScore: Int = 0, var karmaScore : Int = 0, var commentCount: Int = 0) {

    val comments = ArrayList<Comments.Comment>()

    fun include(comment : Comments.Comment){
        if(comment.kudos >= 0)
            kudoScore += comment.kudos
        else
            karmaScore += comment.kudos
        commentCount++
    }

    fun getScore() : Int {
        return kudoScore + karmaScore
    }

}