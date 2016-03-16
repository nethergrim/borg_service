package com.nethergrim.borg.storage

import com.nethergrim.borg.entities.Quote

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */
interface QuotesRepository {

    private object Holder {
        val INSTANCE = QuotesRepositoryH2Impl()
    }

    companion object {
        val instance: QuotesRepositoryH2Impl by lazy { QuotesRepository.Holder.INSTANCE }
    }

    fun saveQuotes(quotes: List<Quote>);

    fun clear();
}

