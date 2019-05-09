package com.stan.scraper.page

import org.jsoup.nodes.Document
import java.net.URL

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-02-08
 * @version 1.0
 */
abstract class PageRequest<T>(private val page: String) {

    fun toURL(base : String) : URL {
        return URL(base.plus(page))
    }

    abstract fun parse(doc: Document) : T
}