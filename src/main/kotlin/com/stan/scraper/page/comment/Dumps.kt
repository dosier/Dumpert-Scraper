package com.stan.scraper.page.comment

import com.google.gson.annotations.Expose

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-13
 * @version 1.0
 */
class Dumps {

    @Expose private val dumps = ArrayList<Dump>()

    /**
     * Add a non-top comment to the [dumps] list.
     *
     * @param dump the [Dump] to add to the list.
     */
    fun add(dump: Dump){
        dumps.add(dump)
    }

    class Dump(@Expose val title: String,
               @Expose val pageId: String,
               @Expose val date: String,
               @Expose val stats: String,
               @Expose val description: String)
}