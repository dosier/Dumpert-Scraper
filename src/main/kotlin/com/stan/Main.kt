package com.stan

import com.google.gson.GsonBuilder
import com.stan.scraper.DumpScraper
import com.stan.scraper.page.request.CommentsPageRequest
import com.stan.scraper.cache.CommentsCache
import java.io.FileWriter
import java.nio.file.Paths

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
object Main {

    private val SCRAPER = DumpScraper<CommentsCache>()

    private val SAVE_PATH = Paths.get("comments")!!

    private val G_SON = GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create()!!

    @JvmStatic fun main(args: Array<String>){
       parsePage("7679071/af4e4f87")
    }

    private fun parsePage(id : String){

        val request = CommentsPageRequest("embed/$id/comments/")

        val results = SCRAPER.scrape("https://comments.dumpert.nl/", listOf(request))

        results.forEach { it.sortByKudos() }

        val fileName = "${id.replace("/", "_")}.json"
        val file = SAVE_PATH.resolve(fileName).toFile()
        val fileWriter = FileWriter(file)

        if(file.createNewFile())
            println("Created new file '$file'!")

        G_SON.toJson(results, fileWriter)

        fileWriter.flush()
        fileWriter.close()
    }
}