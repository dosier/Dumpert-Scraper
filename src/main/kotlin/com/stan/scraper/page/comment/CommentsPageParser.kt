package com.stan.scraper.page.comment

import com.stan.scraper.page.PageParser
import com.stan.scraper.page.comment.Comments.Comment
import org.jsoup.nodes.Document

/**
 * Represents a [PageParser] implementation for [Comments].
 *
 * @param pageId the id of the targeted page.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
class CommentsPageParser(pageId : String) : PageParser<Comments>("embed/$pageId/comments/") {

    override fun parse(doc: Document): Comments {

        /*
         * Creat comments cache to store parsing results.
         */
        val commentCache = Comments()

        val commentEntries = doc.select(COMMENT_LIST)

        for(commentEntry in commentEntries){

            val content = commentEntry.selectFirst(COMMENT_CONTENT).text()
            val username = commentEntry.selectFirst(COMMENT_USERNAME).text()
            val datetime = commentEntry.selectFirst(COMMENT_TIME).text()
            val kudos = commentEntry.selectFirst(COMMENT_KUDOS).text().toInt()

            val comment = Comment(username, content, datetime, kudos)

            if(commentEntry.attr(TOP_COMMENT) == "1")
                commentCache.topComment = comment
            else
                commentCache.add(comment)
        }

        return commentCache
    }

    companion object {

        const val COMMENTS_BASE_URL = "https://comments.dumpert.nl/"

        const val COMMENT_LIST = "article.comment"
        const val COMMENT_CONTENT = "div.cmt-content"
        const val COMMENT_USERNAME = "span.username"
        const val COMMENT_TIME = "span.datetime"
        const val COMMENT_KUDOS = "span.commentkudocount"

        const val TOP_COMMENT = "data-topcomment"
    }
}