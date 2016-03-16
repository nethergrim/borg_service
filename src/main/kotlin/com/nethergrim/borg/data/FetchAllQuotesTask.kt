package com.nethergrim.borg.data

import com.nethergrim.borg.storage.QuotesRepository
import java.util.concurrent.Executors

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */
class FetchAllQuotesTask private constructor() {

    private object Holder {
        val INSTANCE = FetchAllQuotesTask()
    }

    companion object {
        val instance: FetchAllQuotesTask by lazy { FetchAllQuotesTask.Holder.INSTANCE }
    }

    fun fetchAllQuotes(repository: QuotesRepository) {
        println("Fetching all quotes")
        getBorgPage(Int.MAX_VALUE)
        println("Fetching from $lastBorgPageNumber to 1")


        val executor = Executors.newCachedThreadPool();

        for (i in lastBorgPageNumber..1) {
            executor.submit({
                val index = i;
                val quotes = getBorgPage(index)

                println("Fetched page $index, saving to the database")
                repository.save(quotes)
            })
        }
    }
}
