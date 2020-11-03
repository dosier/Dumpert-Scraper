package com.stan.evaluation

import com.stan.scraper.parse.comment.Comments
import com.stan.scraper.parse.dump.Dumps
import java.util.regex.Pattern
import kotlin.math.min

object CommentEvaluation {

    @JvmStatic
    fun main(args: Array<String>) {
//        evaluateUserStats()
//        evaluateWordStats()
    }


    private fun evaluateWordStats() {
        val delimiters = charArrayOf(' ', ',', '.', '?', '!', ':', ';')

        val minOccurences = 10
        val maxResults = 200

        val pageStart = 1
        val pageEnd = 1000

        val evaluatedWords = HashMap<String, WordEvaluator>()

        println("Loaded dump ids, continue loading comments now...")

        Comments.loadByDumps(*Dumps.loadRange(pageStart, pageEnd).toTypedArray())
            .forEach { comments ->
                run {

                    val comIt = comments.comments.iterator()

                    while(comIt.hasNext()){

                        val next = comIt.next()

                        next.content.split(*delimiters).map { it.toLowerCase().trim() }.forEach {

                            if(evaluatedWords[it] != null){
                                evaluatedWords[it]?.evaluate(next)
                            } else
                                evaluatedWords[it] = WordEvaluator.create(next)

                        }

                        val sub = next.subComments

                        if(sub != null){

                            val subIt = sub.iterator()

                            while(subIt.hasNext()){

                                val nextSub = subIt.next()

                                nextSub.content.split(*delimiters).map { it.toLowerCase().trim() }.forEach {

                                    if(evaluatedWords[it] != null){
                                        evaluatedWords[it]?.evaluate(next)
                                    } else
                                        evaluatedWords[it] = WordEvaluator.create(next)

                                }
                            }
                        }
                    }
                }
            }

        evaluatedWords.entries
            .filter { it.value.occurences >= minOccurences}
            .filter { !Pattern.matches("\\d+-\\d+-\\d+", it.key) }
            .sortedByDescending { it.value.getScore() }
            .subList(0, min(maxResults, evaluatedWords.entries.size))
            .forEach { println("word '${it.key}'\n \t score[+${it.value.kudoScore}, ${it.value.karmaScore}] = ${it.value.getScore()}\n \t occurrences = ${it.value.occurences}") }
    }

    private fun evaluateUserStats() {

        val pageStart = 1
        val pageEnd = 10

        val evaluatedUsers = HashMap<String, UserEvaluator>()

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

                                evaluatedUsers.putIfAbsent(nextSub.user, UserEvaluator())
                                evaluatedUsers[nextSub.user]?.evaluate(nextSub)
                            }
                        }
                        evaluatedUsers.putIfAbsent(next.user, UserEvaluator())
                        evaluatedUsers[next.user]?.evaluate(next)
                    }
                }
            }

        evaluatedUsers.entries
            .filter { it.value.getScore() != 0 }
            .sortedBy { it.value.getScore() }
            .forEach { println("user[${it.key}], score[${it.value.kudoScore}, ${it.value.karmaScore}] = ${it.value.getScore()}, comments ${it.value.commentCount}") }

    }
}