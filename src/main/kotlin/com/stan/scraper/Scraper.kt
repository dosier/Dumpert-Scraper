package com.stan.scraper

import com.stan.Configuration
import com.stan.scraper.parse.Parser
import java.math.RoundingMode
import java.net.URL
import java.text.DecimalFormat
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Represents a scraper that parses web pages and parses them to objects of type [T].
 *
 * @see Configuration.threadCount the number of threads the [executorService] is allowed to work with.
 * @see Configuration.delayBetweenPageScrapes the amount of millis to wait between [scraping] iterations.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
class Scraper<T>(private val configuration : Configuration) {

    private val executorService = Executors.newFixedThreadPool(configuration.threadCount)!!
    private val futures = ArrayList<Future<T>>()
    private val data = ArrayList<T>()

    private val counter = AtomicInteger(0)

    /**
     * Start scraping pages from the specified url.
     *
     * @param baseUrl the base url used to fetch pages from.
     * @param parsers a [List] of [Parser] objects that each detail and parse one page.
     *
     * @return a [List] of objects of type [T].
     */
    fun scrape(baseUrl: String, parsers: List<Parser<T>>) : List<T>{

        for(request in parsers)
            submitNewURL(request.toURL(baseUrl), request)

        val total = parsers.size
        var previousPercentage = ""

        val start = System.currentTimeMillis()

        while (scraping()) {

            val left = total - counter.get()
            val percentage = format.format(100 - ((left * 100.0f) / total))

            if(percentage != previousPercentage){
                println("Scraping... $percentage% completed")
                previousPercentage = percentage
            }
        }

        val end = System.currentTimeMillis()
        val duration = TimeUnit.MILLISECONDS.toSeconds(end-start)

        println("Finished scraping, took $duration seconds")
        executorService.shutdown()

        return data
    }

    /**
     * Submit a new page at the specified [URL] for scraping.
     *
     * @param url the page [URL].
     * @param parser the [Parser].
     */
    private fun submitNewURL(url: URL, parser: Parser<T>) {
        val page = Grabber(url, parser)
        val future = executorService.submit(page)
        futures.add(future)
    }

    @Throws(InterruptedException::class)
    private fun scraping(): Boolean {

        Thread.sleep(configuration.delayBetweenPageScrapes)

        val iterator = futures.iterator()

        while (iterator.hasNext()) {

            val future = iterator.next()

            if (future.isDone) {

                iterator.remove()

                counter.incrementAndGet()

                try {
                    data.add(future.get())
                } catch (e: Exception) {
                    System.err.println("Failed to scrape page: ${e.localizedMessage}")
                    e.printStackTrace()
                }
            }
        }
        return futures.size > 0
    }

    companion object {

        private val format = DecimalFormat("#.###")

        init {
            format.roundingMode = RoundingMode.CEILING
        }
    }
}