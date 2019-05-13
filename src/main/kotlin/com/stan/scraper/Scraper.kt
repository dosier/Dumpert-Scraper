package com.stan.scraper

import com.stan.scraper.page.PageParser
import com.stan.scraper.page.PageGrabber
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Represents a scraper that parses web pages and parses them to objects of type [T].
 *
 * @see THREAD_COUNT the amount of threads the [executorService] is allowed to work with.
 * @see PAUSE_TIME the amount of millis to wait between [scraping] iterations.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
class Scraper<T> {

    private val executorService = Executors.newFixedThreadPool(THREAD_COUNT)!!
    private val futures = ArrayList<Future<T>>()
    private val data = ArrayList<T>()

    /**
     * Start scraping pages from the specified url.
     *
     * @param baseUrl the base url used to fetch pages from.
     * @param parsers a [List] of [PageParser] objects that each detail and parse one page.
     *
     * @return a [List] of objects of type [T].
     */
    fun scrape(baseUrl: String, parsers: List<PageParser<T>>) : List<T>{

        for(request in parsers)
            submitNewURL(request.toURL(baseUrl), request)

        while (scraping()) {
            println("Scraping...")
        }

        executorService.shutdown()

        return data
    }

    /**
     * Submit a new page at the specified [URL] for scraping.
     *
     * @param url the page [URL].
     * @param parser the [PageParser].
     */
    private fun submitNewURL(url: URL, parser: PageParser<T>) {
        val page = PageGrabber(url, parser)
        val future = executorService.submit(page)
        futures.add(future)
    }

    @Throws(InterruptedException::class)
    private fun scraping(): Boolean {

        Thread.sleep(PAUSE_TIME)

        val iterator = futures.iterator()

        while (iterator.hasNext()) {

            val future = iterator.next()

            if (future.isDone) {

                iterator.remove()

                try {
                    data.add(future.get())
                } catch (e: Exception) {
//                    System.err.println("Failed to scrape page: ${e.localizedMessage}")
                    e.printStackTrace()
                }
            }
        }
        return futures.size > 0
    }

    companion object {

        const val THREAD_COUNT = 1

        const val PAUSE_TIME = 1_000L
    }
}