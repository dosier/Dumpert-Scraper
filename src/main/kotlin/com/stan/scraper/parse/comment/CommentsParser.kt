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
 * @author  Stan van der Bend
 * @since   2019-05-09
 * @version 1.0
 */
class CommentsParser(private val pageId : String) : Parser<Comments>("embed/$pageId/comments/") {

    override fun parse(doc: Document): Comments {

        /*
         * Creat comments cache to store parsing results.
         */
        val commentCache = Comments(pageId)

        // Select all elements in the document with class article.comment
        val commentEntries = doc.select(COMMENTS)

        // Create a flexible iterator (can move forward and backwards)
        val iterator = commentEntries.listIterator()

        while(iterator.hasNext()){

            val next = iterator.next()!!
            val comment = parseComment(next)

            val subComments = ArrayList<Comment>()

            while (iterator.hasNext()){

                val nextChild = iterator.next()

                if(nextChild.parent().id() == SUB_COMMENTS){

                    subComments.add(parseComment(nextChild))

                } else {

                    iterator.previous()

                    break
                }
            }

            if(subComments.isNotEmpty())
                comment.subComments = subComments

            if(next.attr(TOP_COMMENT) == "1")
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

    companion object {

        const val SUB_COMMENTS = "subcomments"

        const val COMMENTS = "article.comment"
        const val COMMENT_CONTENT = "div.cmt-content"
        const val COMMENT_USERNAME = "span.username"
        const val COMMENT_TIME = "span.datetime"
        const val COMMENT_KUDOS = "span.commentkudocount"

        const val TOP_COMMENT = "data-topcomment"
    }
}