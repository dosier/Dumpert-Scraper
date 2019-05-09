package com.stan.scraper.page

import org.jsoup.Jsoup
import java.net.URL
import java.util.concurrent.Callable


/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-02-08
 * @version 1.0
 */
class PageGrab<T>(private val url: URL, private val request: PageRequest<T>) : Callable<T> {

    override fun call(): T {
        return request.parse(Jsoup
            .connect(url.toExternalForm())
            .timeout(TIMEOUT)
            .followRedirects(true)
            .get())
    }

    companion object {
        const val TIMEOUT = 60_000
    }
}