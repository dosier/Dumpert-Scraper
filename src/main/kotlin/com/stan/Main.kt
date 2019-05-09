package com.stan

import com.google.gson.GsonBuilder
import com.stan.scraper.DumpScraper
import com.stan.scraper.page.comment.CommentsPageRequest
import com.stan.scraper.page.comment.Comments
import java.io.FileWriter
import java.nio.file.Paths

/**
 * This is the entry point of the application.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
object Main {

    /**
     * A [DumpScraper] targeting [Comments].
     */
    private val SCRAPER = DumpScraper<Comments>()

    private val SAVE_PATH = Paths.get("comments")!!

    private val G_SON = GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create()!!

    @JvmStatic fun main(args: Array<String>){

        SAVE_PATH.toFile().mkdir()

        parsePage("7679071/af4e4f87")
    }

    /**
     * This function scrapes and serializes the argued page.
     *
     * @param id the id of the page to parse.
     */
    private fun parsePage(id : String){

        val request = CommentsPageRequest("embed/$id/comments/")

        val results = SCRAPER.scrape("https://comments.dumpert.nl/", listOf(request))

        results.forEach { it.sortByKudos() }

        val fileName = "${id.replace("/", "_")}.json"
        val file = SAVE_PATH.resolve(fileName).toFile()

        file.createNewFile()

        val fileWriter = FileWriter(file)

        G_SON.toJson(results, fileWriter)

        fileWriter.flush()
        fileWriter.close()
    }
}