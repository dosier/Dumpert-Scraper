package com.stan.evaluation

import com.stan.scraper.parse.comment.Comments

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   2019-05-22
 * @version 1.0
 */
class WordEvaluator(
    var occurences: Int,
    var kudoScore: Int,
    var karmaScore : Int
) : CommentEvaluator {

    override fun evaluate(comment: Comments.Comment){
        val positive = comment.kudos >= 0
        if(positive)
            kudoScore += comment.kudos
        else
            karmaScore += comment.kudos
        occurences++
    }

    override fun getScore() : Int {
        return (karmaScore + kudoScore) / occurences
    }

    fun calculateRatio() : Double {
        return Math.abs(if(karmaScore == 0 || kudoScore == 0) 1.0 else kudoScore.toDouble() / karmaScore.toDouble())
    }

    companion object {
        fun create(firstComment : Comments.Comment) : WordEvaluator {
            val positive = firstComment.kudos >= 0
            return WordEvaluator(1, if(positive) firstComment.kudos else 0, if(!positive) firstComment.kudos else 0)
        }
    }
}