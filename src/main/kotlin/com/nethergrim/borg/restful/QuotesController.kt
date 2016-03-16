package com.nethergrim.borg.restful

import com.nethergrim.borg.data.FetchAllQuotesTask
import com.nethergrim.borg.data.parseBorgPageFromTop
import com.nethergrim.borg.entities.Quote
import com.nethergrim.borg.entities.QuotesResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */
@RestController(value = "/borg/quotes")
open class QuotesController {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun handleGetQuotes(@RequestParam (value = "limit", defaultValue = "20") limit: String,
                        @RequestParam (value = "offset", defaultValue = "0") offset: String): QuotesResponse {
        return QuotesResponse(emptyList<Quote>(), "result for limit $limit and offset $offset")
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/top")
    fun handleGetTopQuotes(@RequestParam (value = "limit", defaultValue = "20") limit: String,
                           @RequestParam (value = "offset", defaultValue = "0") offset: String): QuotesResponse {
        return QuotesResponse(parseBorgPageFromTop(1), "result for limit $limit and offset $offset")
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/random")
    fun handleGetRandomQuotes(@RequestParam (value = "limit", defaultValue = "20") limit: String,
                              @RequestParam (value = "offset", defaultValue = "0") offset: String): QuotesResponse {
        return QuotesResponse(parseBorgPageFromTop(1), "result for limit $limit and offset $offset")
    }


    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun handlePostQuotes(): QuotesResponse {
        FetchAllQuotesTask.instance.fetchAllQuotes();
        return QuotesResponse(emptyList<Quote>(), "fetched all quotes")
    }

}