package com.stan.scraper.parse

import org.jsoup.nodes.Document
import java.net.URL

/**
 * Represents a page parser.
 *
 * @param urlAppendix the value appended to the base url (to target a specific page).
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-02-08
 * @version 1.0
 */
abstract class Parser<T>(private val urlAppendix: String) {

    /**
     * Create an [URL] from the specified base and [urlAppendix].
     *
     * @param base the base for the [URL].
     *
     * @return a new [URL] object consisting of the [base] plus [urlAppendix].
     */
    fun toURL(base : String) : URL {
        return URL(base.plus(urlAppendix))
    }

    /**
     * Parses a [Document] and returns a [T].
     *
     * @param doc the [Document] retrieved from the page at the created [URL].
     *
     * @see [toURL] for the [URL] creation of the target page.
     *
     * @return a new [T] created as a result of parsing the [doc].
     */
    abstract fun parse(doc: Document) : T
}