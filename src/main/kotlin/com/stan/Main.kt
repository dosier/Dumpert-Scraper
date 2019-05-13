package com.stan

import com.stan.scraper.Scraper
import com.stan.scraper.parse.comment.CommentsParser
import com.stan.scraper.parse.comment.Comments
import com.stan.scraper.parse.dump.Dumps
import com.stan.scraper.parse.dump.DumpsParser
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

        val pageIds = ArrayList<String>()

        Dumps.loadRange(1, 10)
            .forEach { pageIds.addAll(it.getPageIds()) }

        val comments = ArrayList<Comments.Comment>()

        Comments.load(*pageIds.toTypedArray())
            .forEach { comments.addAll(it.comments) }

        printTopUsers(comments)
    }

    private fun printTopUsers(comments: ArrayList<Comments.Comment>) {
        comments
            .groupBy { it.user }
            .mapValues { it.value.sumBy { comment -> comment.kudos } }
            .toList()
            .sortedByDescending { it.second }
            .forEach { println("User:  ${it.first} \n \t kudos = ${it.second}") }
    }

    /**
     * This function scrapes and serializes all pages with the specified ids.
     *
     * @param pageIds the ids of the pages to parse.
     */
    private fun scrapeAndSerializeComments(vararg pageIds : String){

        val config = Configuration.load()

        val scraper = Scraper<Comments>(config)
        val parsers = pageIds.map { CommentsParser(it) }
        val results = scraper.scrape(Comments.BASE_URL, parsers)

        results.forEach { it.sortByKudos() }
        results.forEach { Serializer.serialize("${Comments.BASE_PATH}/$it", it) }
    }

    /**
     * This function scrapes and serializes all dump pages.
     */
    private fun scrapeAndSerializeDumps(){

        val config = Configuration.load()

        val scraper = Scraper<Dumps>(config)

        val parsers = IntStream.rangeClosed(1, DumpsParser.DUMP_PAGE_COUNT)
            .mapToObj { DumpsParser(it) }
            .toList()

        val results = scraper.scrape(Dumps.BASE_URL, parsers)

        results.forEach { Serializer.serialize("${Dumps.BASE_PATH}/$it", it) }
    }

}