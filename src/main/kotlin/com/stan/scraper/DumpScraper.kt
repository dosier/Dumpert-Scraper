package com.stan.scraper

import com.stan.scraper.page.PageRequest
import com.stan.scraper.page.PageGrab
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-09
 * @version 1.0
 */
class DumpScraper<T> {

    private val executorService = Executors.newFixedThreadPool(THREAD_COUNT)!!
    private val futures = ArrayList<Future<T>>()
    private val data = ArrayList<T>()

    fun scrape(start: String, requests: List<PageRequest<T>>) : List<T>{

        for(request in requests)
            submitNewURL(request.toURL(start), request)

        while (checkPageGrabs()){

            if(abort) {
                executorService.shutdownNow()
                futures.clear()
                abort = false
                break
            }

        }

        println("Parsed " + data.size + " pages")
        return data
    }

    @Throws(InterruptedException::class)
    private fun checkPageGrabs(): Boolean {
        Thread.sleep(PAUSE_TIME)
        val iterator = futures.iterator()

        while (iterator.hasNext()) {
            val future = iterator.next()
            if (future.isDone) {
                iterator.remove()
                try {
                    data.add(future.get())
                } catch (e: Exception) {
                    System.err.println("Failed to scrape page: ${e.localizedMessage}")
                }
            }
        }
        return futures.size > 0
    }

    private fun submitNewURL(url: URL, request: PageRequest<T>) {
        val page = PageGrab(url, request)
        val future = executorService.submit(page)
        futures.add(future)
    }

    companion object {
        const val THREAD_COUNT = 1
        const val PAUSE_TIME = 100L
        var abort = false
    }
}