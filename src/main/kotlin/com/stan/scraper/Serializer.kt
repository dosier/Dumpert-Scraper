package com.stan.scraper

import com.google.gson.GsonBuilder
import java.io.FileWriter
import java.nio.file.Paths

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-10
 * @version 1.0
 */
object Serializer {

    private val SAVE_PATH = Paths.get("comments")!!

    private val G_SON = GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create()!!

    /**
     * Serializes a [List] of results from the scraping process to the argued file.
     *
     * @param fileName the name of the file (excluding file extension).
     * @param results a [List] of results to serialize.
     */
    fun<T> serialize(fileName: String, results: List<T>) {

        SAVE_PATH.toFile().mkdir()

        val file = SAVE_PATH.resolve("$fileName.json").toFile()

        file.createNewFile()

        val fileWriter = FileWriter(file)

        G_SON.toJson(results, fileWriter)

        fileWriter.flush()
        fileWriter.close()
    }

}