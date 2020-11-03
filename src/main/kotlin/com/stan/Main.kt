package com.stan

import com.stan.scraper.Scraper
import com.stan.scraper.parse.comment.Comments
import com.stan.scraper.parse.comment.CommentsParser
import com.stan.scraper.parse.dump.Dumps
import com.stan.scraper.parse.dump.DumpsParser
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream
import kotlin.streams.toList

/**
 * This is the entry point of the application.
 *
 * @author  Stan van der Bend
 * @since   2019-05-09
 * @version 1.0
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>){

        val startTime = System.nanoTime()

        val results = scrapeAndSerializeDumps()

        results.forEach { Serializer.serialize("${Dumps.BASE_PATH}/$it", it) }

        val pageIds = ArrayList<String>()

        results.forEach { pageIds.addAll(it.getPageIds()) }

        scrapeAndSerializeComments(*pageIds.toTypedArray())

        val endTime = System.nanoTime()

        val s = TimeUnit.NANOSECONDS.toSeconds(endTime-startTime)

        println("###################################################")
        println("#          TOTAL RUN TIME WAS $s SECONDS           #")
        println("###################################################")
    }

    /**
     * This function scrapes and serializes all pages with the specified ids.
     *
     * @param pageIds the ids of the pages to parse.
     */
    private fun scrapeAndSerializeComments(vararg pageIds : String){

        println("Scraping comments from ${pageIds.size} pages")

        val config = Configuration.load()

        val scraper = Scraper<Comments>(config)
        val parsers = pageIds.map { CommentsParser(it) }
        val results = scraper.scrape(Comments.BASE_URL, parsers)

        results.forEach { it.sortByKudos() }
        results.forEach { Serializer.serialize("${Comments.BASE_PATH}/$it", it) }

        println()
        println()
    }

    /**
     * This function scrapes and serializes all dump pages.
     */
    private fun scrapeAndSerializeDumps() : List<Dumps> {

        println("Scraping dumps")

        val config = Configuration.load()

        val scraper = Scraper<Dumps>(config)

        val parsers = IntStream.rangeClosed(config.startPage, config.endPage)
            .mapToObj { DumpsParser(it) }
            .toList()

        println()
        println()

        return scraper.scrape(Dumps.BASE_URL, parsers)
    }

}