import com.stan.evaluation.UserEvaluation
import com.stan.evaluation.WordEvaluation
import com.stan.scraper.parse.comment.Comments
import com.stan.scraper.parse.dump.Dumps
import java.util.regex.Pattern
import kotlin.math.min
import org.junit.Test as test
/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @since   2019-05-28
 * @version 1.0
 */
class EvaluationTest {

    @test fun evaluateWordStats() {
        val delimiters = charArrayOf(' ', ',', '.', '?', '!', ':', ';')

        val minOccurences = 10
        val maxResults = 200

        val pageStart = 1
        val pageEnd = 1000

        val evaluatedWords = HashMap<String, WordEvaluation>()

        println("Loaded dump ids, continue loading comments now...")

        Comments.loadByDumps(*Dumps.loadRange(pageStart, pageEnd).toTypedArray())
            .forEach { comments ->
                run {

                    val comIt = comments.comments.iterator()

                    while(comIt.hasNext()){

                        val next = comIt.next()

                        next.content.split(*delimiters).map { it.toLowerCase().trim() }.forEach {

                            if(evaluatedWords[it] != null){
                                evaluatedWords[it]?.include(next)
                            } else
                                evaluatedWords[it] = WordEvaluation.create(next)

                        }

                        val sub = next.subComments

                        if(sub != null){

                            val subIt = sub.iterator()

                            while(subIt.hasNext()){

                                val nextSub = subIt.next()

                                nextSub.content.split(*delimiters).map { it.toLowerCase().trim() }.forEach {

                                    if(evaluatedWords[it] != null){
                                        evaluatedWords[it]?.include(next)
                                    } else
                                        evaluatedWords[it] = WordEvaluation.create(next)

                                }
                            }
                        }
                    }
                }
            }

        evaluatedWords.entries
            .filter { it.value.occurences >= minOccurences}
            .filter { !Pattern.matches("\\d+-\\d+-\\d+", it.key) }
            .sortedByDescending { it.value.calculateScore() }
            .subList(0, min(maxResults, evaluatedWords.entries.size))
            .forEach { println("word '${it.key}'\n \t score[+${it.value.kudoScore}, ${it.value.karmaScore}] = ${it.value.calculateScore()}\n \t occurrences = ${it.value.occurences}") }
    }

    @test fun evaluateUserStats() {

        val pageStart = 1
        val pageEnd = 10

        val evaluatedUsers = HashMap<String, UserEvaluation>()

        println("Loaded dump ids, continue loading comments now...")

        Comments.loadByDumps(*Dumps.loadRange(pageStart, pageEnd).toTypedArray())
            .forEach { comments ->
                run {

                    val comIt = comments.comments.iterator()

                    while(comIt.hasNext()){

                        val next = comIt.next()
                        val sub = next.subComments

                        if(sub != null){

                            val subIt = sub.iterator()

                            while(subIt.hasNext()){

                                val nextSub = subIt.next()

                                evaluatedUsers.putIfAbsent(nextSub.user, UserEvaluation())
                                evaluatedUsers[nextSub.user]?.include(nextSub)
                            }
                        }
                        evaluatedUsers.putIfAbsent(next.user, UserEvaluation())
                        evaluatedUsers[next.user]?.include(next)
                    }
                }
            }

        evaluatedUsers.entries
            .filter { it.value.getScore() != 0 }
            .sortedBy { it.value.getScore() }
            .forEach { println("user[${it.key}], score[${it.value.kudoScore}, ${it.value.karmaScore}] = ${it.value.getScore()}, comments ${it.value.commentCount}") }

    }
}