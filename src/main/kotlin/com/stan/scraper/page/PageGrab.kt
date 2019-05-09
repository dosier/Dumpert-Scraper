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

        return request.parse( Jsoup.connect(url.toExternalForm())
            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
            .timeout(60_000)
            .followRedirects(true)
            .get())
    }

    companion object {
        const val TIMEOUT = 60_000
    }
}