package com.stan.scraper.parse.dump

import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import com.stan.Serializer

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-13
 * @version 1.0
 */
class Dumps(private val pageIndex : Int) {

    @Expose private val dumps = ArrayList<Dump>()

    /**
     * Add a non-top comment to the [dumps] list.
     *
     * @param dump the [Dump] to add to the list.
     */
    fun add(dump: Dump){
        dumps.add(dump)
    }

    fun getPageIds() : List<String>{
        return dumps.map { it.pageId }
    }

    override fun toString(): String {
        return "$pageIndex"
    }

    class Dump(
        @Expose val pageId: String,
        @Expose val title: String,
        @Expose val date: String,
        @Expose val stats: String,
        @Expose val description: String
    )

    companion object {

        const val BASE_URL = "https://www.dumpert.nl/"
        const val BASE_PATH = "dumps"

        private val type = object : TypeToken<Dumps>(){}.type!!

        fun load(pageIndex: Int) : Dumps {
            return Serializer.deserialize("$BASE_PATH/$pageIndex", type)
        }
    }
}