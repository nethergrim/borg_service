package com.nethergrim.borg.entities

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */

data class QuotesResponse(val quotes: Array<Quote>, val message: String?)


data class Quote(val id: Long, val quote: String, val rating: Int, val date: String)
