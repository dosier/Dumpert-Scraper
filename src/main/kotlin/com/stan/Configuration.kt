package com.stan

import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-13
 * @version 1.0
 *
 * @param threadCount the numbers of threads that the scraper can use.
 * @param delayBetweenPageScrapes the delay between each consecutive scrape batch (in millis).
 *
 */
data class Configuration(@Expose val threadCount: Int = 1,
                         @Expose val delayBetweenPageScrapes : Long = 1_000L,
                         @Expose val startPage : Int = 1,
                         @Expose val endPage : Int = 1){

    companion object {

        private const val FILE_NAME = "config"

        private val type = object : TypeToken<Configuration>() {}.type!!

        fun load() : Configuration {

            val config = if(!Serializer.exists(FILE_NAME)) {
                println("[Configuration]: load -> did not find configuration file, using default values...")
                Configuration()
            } else
                Serializer.deserialize(FILE_NAME, type)

            println("[Configuration]: load -> using the following configuration:")
            println()
            println(config)
            println()

            return config
        }
    }

    override fun toString(): String {
        return "Configuration(threadCount=$threadCount, delayBetweenPageScrapes=$delayBetweenPageScrapes)"
    }


}