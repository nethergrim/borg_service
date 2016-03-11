package com.nethergrim.borg.data

import com.nethergrim.borg.entities.Quote
import org.apache.commons.lang3.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * *         All rights reserved.
 */
object BashorgParser {

    val INPUT = "input"
    val CLASS = "class"
    val PAGE = "page"
    val VALUE = "value"
    val MAX = "max"
    val A = "a"
    val ID = "id"
    val DIV = "div"
    val TEXT = "text"
    val BR = "<br>"
    val EMPTY_STRING = ""
    val SPAN = "span"
    val DATE = "date"
    val RATING = "rating"
    val THREE_DOTS = "..."
    val QUESTION = "?"
    val URI_QUOTE = "content://com.nethergrim.bashorg.web.uri.QUOTE"
    val URL_BASHORG_PAGE = "http://bash.im/index/"
    val URL_BASHORG_TOP = "http://bash.im/byrating/"
    val URL_BASHORG_RANDOM_PAGE = "http://bash.im/random"
    private val _random = Random()
    private val DEFAULT_PAGE_SIZE = 50

    var lastPageNumber: Int = -1;

    fun getPage(pageNumber: Int): List<Quote> {
        if (pageNumber == 0) {
            return ArrayList()
        }
        return BashorgParser.parsePage(pageNumber.toString())
    }

    fun parsePage(pageNumber: String): List<Quote> {
        try {
            println("fetching and parsing page: $pageNumber")
            val document = Jsoup.connect(URL_BASHORG_PAGE + pageNumber).get()
            lastPageNumber = getPageNumber(document)
            println("real last page number: $lastPageNumber")
            return parseDocument(document)
        } catch (e: Exception) {
            e.printStackTrace()
            return ArrayList()
        }
    }


    fun parsePrevPage(): List<Quote>? {
        if (lastPageNumber <= 0) {
            return null
        }
        println("fetching and parsing next page (${lastPageNumber - 1})")
        return getPage(lastPageNumber - 1);
    }

    fun parsePageFromTop(byRatingPage: Int): List<Quote> {
        try {
            println("fetching and parsing page from top: $byRatingPage")
            val document = Jsoup.connect(URL_BASHORG_TOP + byRatingPage.toString()).get()
            return parseDocument(document)
        } catch (e: IOException) {
            e.printStackTrace()
            return ArrayList()
        }
    }


    fun getRandomPage(): List<Quote> {
        try {
            println("fetching and parsing random page")
            val document = Jsoup.connect(URL_BASHORG_RANDOM_PAGE + QUESTION + _random.nextLong()).get()
            return parseDocument(document)
        } catch (e: Exception) {
            e.printStackTrace()
            return ArrayList()
        }

    }

    fun getPageNumber(document: Document): Int {
        val elements = document.select(INPUT)
        var i = 0
        val size = elements.size
        while (i < size) {
            val element = elements[i]
            if (element.attr(CLASS) == PAGE) {
                val currentPage = element.attr(VALUE)
                val maxPage = element.attr(MAX)
                return Integer.parseInt(currentPage)
            }
            i++
        }
        return -1
    }

    fun getIds(document: Document): LongArray {
        val array = LongArray(DEFAULT_PAGE_SIZE)
        var counter = 0
        val numberElements = document.select(A)
        var i = 0
        val size = numberElements.size
        while (i < size) {
            val numberElement = numberElements[i]
            if (numberElement.attr(CLASS) == ID) {
                var text = numberElement.html()
                text = text.substring(1, text.length)
                array[counter] = java.lang.Long.parseLong(text)
                counter++
            }
            i++
        }
        return array
    }

    fun getTexts(document: Document): List<String> {
        val array = ArrayList<String>()
        val divs = document.select(DIV)
        var i = 0
        val size = divs.size
        while (i < size) {
            val element = divs[i]
            if (element.attr(CLASS) == TEXT) {
                var text = element.html()
                text = text.replace(BR, EMPTY_STRING)
                text = StringEscapeUtils.unescapeHtml4(text)
                array.add(text)
            }
            i++
        }
        return array
    }

    fun getDates(document: Document): List<String> {
        val array = ArrayList<String>()
        var counter = 0
        val divs = document.select(SPAN)

        var i = 0
        val size = divs.size
        while (i < size) {
            val element = divs[i]
            if (element.attr(CLASS) == DATE) {
                array.add(element.html());
                counter++
            }
            i++
        }
        return array
    }

    fun getRatings(document: Document): IntArray {
        val array = IntArray(DEFAULT_PAGE_SIZE)
        val spans = document.select(SPAN)
        var counter = 0

        var i = 0
        val size = spans.size
        while (i < size) {
            val span = spans[i]
            if (span.attr(CLASS) == RATING) {
                if (span.html() == THREE_DOTS) {
                    array[counter] = 0
                    i++
                    continue
                }
                try {
                    val rating = span.html()
                    array[counter] = Integer.parseInt(rating)
                } catch (e: NumberFormatException) {
                    array[counter] = 0
                    e.printStackTrace()
                }

                counter++
            }
            i++
        }
        return array
    }

    private fun parseDocument(document: Document): List<Quote> {
        val result = ArrayList<Quote>(100)
        try {
            val ids = getIds(document)
            val ratings = getRatings(document)
            val texts = getTexts(document)
            val dates = getDates(document)
            val size = ids.size
            for (i in 0..size - 2) {
                val quote = Quote(ids[i], texts[i], ratings[i], dates[i])
                result.add(quote)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }
}