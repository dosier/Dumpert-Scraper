package com.stan.scraper

import com.stan.scraper.page.comment.CommentsPageParser
import com.stan.scraper.page.comment.Comments
import com.stan.scraper.page.comment.CommentsPageParser.Companion.COMMENTS_BASE_URL

/**
 * This is the entry point of the application.
 *
 * TODO: add automatic page id listing (maybe use video navigation).
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>){
        scrapeAndSerializeComments("7679071/af4e4f87")
        scrapeAndSerializeComments("7678735/80d52f3f")
    }

    /**
     * This function scrapes and serializes the page with the argued id.
     *
     * @param pageId the id of the page to parse.
     */
    private fun scrapeAndSerializeComments(pageId : String){

        val scraper = Scraper<Comments>()
        val requests = listOf(CommentsPageParser(pageId))
        val results = scraper.scrape(COMMENTS_BASE_URL, requests)

        results.forEach { it.sortByKudos() }

        Serializer.serialize(pageId.replace("/", "_"), results)
    }

}