package com.stan

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stan.scraper.parse.comment.Comments
import com.stan.scraper.parse.dump.Dumps
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Represents a serializer object, can be used to (de-)serialize scrape results.
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

    init {
        SAVE_PATH.toFile().mkdirs()
        SAVE_PATH.resolve(Dumps.BASE_PATH).toFile().mkdir()
        SAVE_PATH.resolve(Comments.BASE_PATH).toFile().mkdir()
    }

    fun exists(fileName: String) : Boolean {
        return SAVE_PATH.resolve("$fileName.json").toFile().exists()
    }

    /**
     * Serializes a result of type [T] to the argued file.
     *
     * @param fileName the name of the file (excluding file extension).
     * @param result the [T] result to serialize.
     */
    fun<T> serialize(fileName: String, result: T) {

        SAVE_PATH.toFile().mkdir()

        val file = SAVE_PATH.resolve("$fileName.json").toFile()

        file.createNewFile()

        val fileWriter = FileWriter(file)

        GSON.toJson(result, fileWriter)

        fileWriter.flush()
        fileWriter.close()
    }

    /**
     * De-serializes a result of type [T] from the argued file.
     *
     * @param fileName the name of the file (excluding file extension).
     *
     * @return a result of type [T].
     */
    fun<T> deserialize(fileName: String, type : Type) : T {

        val file = SAVE_PATH.resolve("$fileName.json").toFile()

        val fileReader = FileReader(file)

        val result = GSON.fromJson<T>(fileReader, type)

        fileReader.close()

        return result
    }

}