package com.stan.scraper.page.comment

import com.stan.scraper.page.PageParser
import org.jsoup.nodes.Document

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-13
 * @version 1.0
 */
class DumpsPageParser(page: Int) : PageParser<Dumps>("toppers/$page/") {

    override fun parse(doc: Document): Dumps {

        val dumps = Dumps()

        val pageContainer = doc.selectFirst(DUMPS_CONTENT)

        val dumpEntries= pageContainer.getElementsByClass(DUMP_CLASS)

        for(dumpEntry in dumpEntries){

            val title = dumpEntry.select("title").text()
            val pageId = dumpEntry.select("a.href").text()
                .substringAfter("mediabase/")
                .substringBeforeLast("/")

            val details = dumpEntry.selectFirst(DUMP_DETAILS)
            val dumpDate = details.selectFirst("date").text()
            val dumpStats = details.selectFirst("p.stats").text()
            val dumpDescription = details.selectFirst("p.description").text()

            dumps.add(Dumps.Dump(title, pageId, dumpDate, dumpStats, dumpDescription))
        }

        return dumps
    }

    companion object {

        const val DUMPS_BASE_URL = "https://www.dumpert.nl/"

        const val DUMP_PAGE_COUNT = 10_000

        const val DUMPS_CONTENT = "section.dump-cnt"
        const val DUMP_CLASS = "dumpthumb"
        const val DUMP_DETAILS = "div.details"
    }

}