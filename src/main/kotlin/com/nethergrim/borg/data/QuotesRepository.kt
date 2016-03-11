package com.nethergrim.borg.data

import com.nethergrim.borg.entities.Quote
import rx.Observable
import java.util.*

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */

object QuotesRepository {

    val cache = HashMap<Long, Quote>(500)

    fun saveQuotes(quotes: List<Quote>) {
        quotes.forEach { cache.put(it.id, it) }
        println("quotes saved. Current cache size: ${cache.size}")
    }

    fun getQuote(id: Long): Quote? {
        if (cache.containsKey(id)) {
            return cache[id];
        }

        return null;
    }

    fun getRandomQuotes(size: Int): List<Quote?> {
        return Observable.from(cache.keys)
                .take(size)
                .map { it -> cache[it] }
                .toList()
                .toBlocking()
                .first()

    }
}
