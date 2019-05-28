package com.stan.evaluation

import com.stan.scraper.parse.comment.Comments

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-22
 * @version 1.0
 */
class WordEvaluation(var occurences: Int, var kudoScore: Int, var karmaScore : Int) {

    fun include(comment: Comments.Comment){
        val positive = comment.kudos >= 0
        if(positive)
            kudoScore += comment.kudos
        else
            karmaScore += comment.kudos
        occurences++
    }

    fun calculateScore() : Int {
        return (karmaScore + kudoScore) / occurences
    }

    fun calculateRatio() : Double {
        return Math.abs(if(karmaScore == 0 || kudoScore == 0) 1.0 else kudoScore.toDouble() / karmaScore.toDouble())
    }

    companion object {
        fun create(firstComment : Comments.Comment) : WordEvaluation {
            val positive = firstComment.kudos >= 0
            return WordEvaluation(1, if(positive) firstComment.kudos else 0, if(!positive) firstComment.kudos else 0)
        }
    }
}