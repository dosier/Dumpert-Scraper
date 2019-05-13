package com.stan

import com.stan.scraper.Scraper
import com.stan.scraper.Serializer
import com.stan.scraper.page.comment.CommentsPageParser
import com.stan.scraper.page.comment.Comments
import com.stan.scraper.page.comment.Dumps
import com.stan.scraper.page.comment.DumpsPageParser
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

        val dumps = Dumps.load(1)

        scrapeAndSerializeComments(*dumps.getPageIds().toTypedArray())


    }

    /**
     * This function scrapes and serializes all pages with the specified ids.
     *
     * @param pageIds the ids of the pages to parse.
     */
    private fun scrapeAndSerializeComments(vararg pageIds : String){

        val scraper = Scraper<Comments>()
        val parsers = pageIds.map { CommentsPageParser(it) }
        val results = scraper.scrape(Comments.BASE_URL, parsers)

        results.forEach { it.sortByKudos() }
        results.forEach { Serializer.serialize("${Comments.BASE_PATH}/$it", it) }
    }

    /**
     * This function scrapes and serializes all dump pages.
     */
    private fun scrapeAndSerializeDumps(){

        val scraper = Scraper<Dumps>()

        val parsers = IntStream.rangeClosed(1, DumpsPageParser.DUMP_PAGE_COUNT)
            .mapToObj { DumpsPageParser(it) }
            .toList()

        val results = scraper.scrape(Dumps.BASE_URL, parsers)

        results.forEach { Serializer.serialize("${Dumps.BASE_PATH}/$it", it) }
    }

}