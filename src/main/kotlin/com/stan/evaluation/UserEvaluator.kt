package com.stan.evaluation

import com.stan.scraper.parse.comment.Comments

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   2019-05-23
 * @version 1.0
 */
class UserEvaluator(
    var kudoScore: Int = 0,
    var karmaScore : Int = 0,
    var commentCount: Int = 0
) : CommentEvaluator {

    override fun evaluate(comment : Comments.Comment){
        if(comment.kudos >= 0)
            kudoScore += comment.kudos
        else
            karmaScore += comment.kudos
        commentCount++
    }

    override fun getScore() = kudoScore + karmaScore
}