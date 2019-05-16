package com.stan.scraper.parse.comment

import com.stan.scraper.parse.Parser
import com.stan.scraper.parse.comment.Comments.Comment
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * Represents a [Parser] implementation for [Comments].
 *
 * @param pageId the id of the targeted page.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
class CommentsParser(private val pageId : String) : Parser<Comments>("embed/$pageId/comments/") {

    override fun parse(doc: Document): Comments {

        /*
         * Creat comments cache to store parsing results.
         */
        val commentCache = Comments(pageId)

        val commentEntries = doc.select(COMMENT_LIST)
        val subcommentEntries = doc.select("div.subcomments")

        val iterator = subcommentEntries.iterator()

        for(commentEntry in commentEntries){

            val comment = parseComment(commentEntry)

            if(iterator.hasNext())
                comment.subComments = parseSubComments(iterator.next())

            if(commentEntry.attr(TOP_COMMENT) == "1")
                commentCache.topComment = comment
            else
                commentCache.add(comment)
        }

        return commentCache
    }

    private fun parseComment(commentEntry: Element): Comment {
        val content = commentEntry.selectFirst(COMMENT_CONTENT).text()
        val username = commentEntry.selectFirst(COMMENT_USERNAME).text()
        val datetime = commentEntry.selectFirst(COMMENT_TIME).text()
        val kudos = commentEntry.selectFirst(COMMENT_KUDOS).text().toInt()

        return Comment(username, content, datetime, kudos)
    }

    private fun parseSubComments(subCommentEntry : Element) : List<Comment> {
        return subCommentEntry.select(COMMENT_LIST).map { parseComment(it) }
    }

    companion object {

        const val COMMENT_LIST = "article.comment"
        const val COMMENT_CONTENT = "div.cmt-content"
        const val COMMENT_USERNAME = "span.username"
        const val COMMENT_TIME = "span.datetime"
        const val COMMENT_KUDOS = "span.commentkudocount"

        const val TOP_COMMENT = "data-topcomment"
    }
}