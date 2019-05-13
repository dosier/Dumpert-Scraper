package com.stan.scraper.parse.dump

import com.stan.scraper.parse.Parser
import org.jsoup.nodes.Document

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-13
 * @version 1.0
 */
class DumpsParser(private val pageIndex: Int) : Parser<Dumps>("toppers/$pageIndex/") {

    override fun parse(doc: Document): Dumps {

        val dumps = Dumps(pageIndex)

        val pageContainer = doc.selectFirst(DUMPS_CONTENT)

        val dumpEntries= pageContainer.getElementsByClass(DUMP_CLASS)

        for(dumpEntry in dumpEntries){

            val pageId = dumpEntry.selectFirst("a").attr("href")
                .substringAfter("mediabase/")
                .substringBeforeLast("/")

            val details = dumpEntry.selectFirst(DUMP_DETAILS)
            val dumpTitle = dumpEntry.select("h1").text()
            val dumpDate = details.selectFirst("date").text()
            val dumpStats = details.selectFirst("p.stats").text()
            val dumpDescription = details.selectFirst("p.description").text()

            dumps.add(Dumps.Dump(pageId, dumpTitle, dumpDate, dumpStats, dumpDescription))
        }

        return dumps
    }

    companion object {

        const val DUMP_PAGE_COUNT = 10_000

        const val DUMPS_CONTENT = "section.dump-cnt"
        const val DUMP_CLASS = "dumpthumb"
        const val DUMP_DETAILS = "div.details"
    }

}