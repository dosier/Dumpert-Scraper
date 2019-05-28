package com.stan.scraper

import com.stan.scraper.parse.Parser
import org.jsoup.Jsoup
import java.net.URL
import java.util.concurrent.Callable


/**
 * Represents a page grabber that returns an object of type [T].
 *
 * @param url the [URL] to grab the page at.
 * @param parser the [Parser] used to parse the grabbed page.
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-02-08
 * @version 1.0
 */
class Grabber<T>(private val url: URL, private val parser: Parser<T>) : Callable<T> {

    override fun call(): T {
        return parser.parse(Jsoup
            .connect(url.toExternalForm())
            .timeout(TIMEOUT)
            .cookie("cpc", "bla")
            .cookie("nsfw", "1")

            .followRedirects(true)
            .get())
    }

    companion object {
        const val TIMEOUT = 60_000
    }
}