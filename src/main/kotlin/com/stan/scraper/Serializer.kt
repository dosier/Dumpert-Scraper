package com.stan.scraper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.FileWriter
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Represents a serializer object, can be used to serialize scrape results.
 *
 * @see SAVE_PATH   the base [Path] to store serialized files at.
 * @see GSON        the [Gson] implementation used to format the data.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-10
 * @version 1.0
 */
object Serializer {

    private val SAVE_PATH = Paths.get("data")!!

    private val GSON = GsonBuilder()
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

        GSON.toJson(results, fileWriter)

        fileWriter.flush()
        fileWriter.close()
    }

}