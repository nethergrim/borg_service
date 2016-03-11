package com.nethergrim.borg.restful

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


        val q1 = Quote(1, "лол тут цитатка", 5, "10.05.1990", System.currentTimeMillis())
        val q2 = Quote(2, "лол тут цитатка ", 52, "10.05.1990", System.currentTimeMillis())
        val q3 = Quote(3, "лол тут цитатка  ", 51, "10.05.1990", System.currentTimeMillis())
        val q4 = Quote(4, "лол тут цитатка  ", 15, "10.05.1990", System.currentTimeMillis())

        return QuotesResponse(arrayOf(q1, q2, q3, q4), "result for limit $limit and offset $offset")
    }

}