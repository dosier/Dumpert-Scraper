package com.stan.evaluation

import com.stan.scraper.parse.comment.Comments

interface CommentEvaluator {

    fun evaluate(comment: Comments.Comment)

    fun getScore(): Int

}