package com.stan.scraper

import com.stan.scraper.page.comment.CommentsPageParser
import com.stan.scraper.page.comment.Comments
import com.stan.scraper.page.comment.CommentsPageParser.Companion.COMMENTS_BASE_URL
import com.stan.scraper.page.comment.Dumps
import com.stan.scraper.page.comment.DumpsPageParser
import com.stan.scraper.page.comment.DumpsPageParser.Companion.DUMPS_BASE_URL
import java.util.stream.IntStream
import kotlin.streams.toList

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
//        scrapeAndSerializeComments("7679071/af4e4f87")
//        scrapeAndSerializeComments("7678735/80d52f3f")
        scrapeAndSerializeDumps()
    }

    /**
     * This function scrapes and serializes the page with the argued id.
     *
     * @param pageId the id of the page to parse.
     */
    private fun scrapeAndSerializeComments(pageId : String){

        val scraper = Scraper<Comments>()
        val parsers = listOf(CommentsPageParser(pageId))
        val results = scraper.scrape(COMMENTS_BASE_URL, parsers)

        results.forEach { it.sortByKudos() }

        Serializer.serialize(pageId.replace("/", "_"), results)
    }

    /**
     * This function scrapes and serializes the page with the argued id.
     *
     * @param pageId the id of the page to parse.
     */
    private fun scrapeAndSerializeDumps(){

        val scraper = Scraper<Dumps>()

        val parsers = IntStream.range(1, DumpsPageParser.DUMP_PAGE_COUNT)
            .mapToObj { DumpsPageParser(it) }
            .toList()

        val results = scraper.scrape(DUMPS_BASE_URL, parsers)
    }

}