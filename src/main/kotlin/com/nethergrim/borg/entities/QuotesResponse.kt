package com.nethergrim.borg.entities

import org.springframework.data.annotation.Id

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */

data class QuotesResponse(val quotes: List<Quote>, val message: String?)


data class Quote(@Id val id: Long, val quote: String, val rating: Int, val date: String)
