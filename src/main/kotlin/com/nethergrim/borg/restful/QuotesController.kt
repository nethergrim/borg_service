package com.nethergrim.borg.restful

import com.nethergrim.borg.data.QuotesRepository
import com.nethergrim.borg.data.getBorgPage
import com.nethergrim.borg.data.lastBorgPageNumber
import com.nethergrim.borg.data.parseBorgPageFromTop
import com.nethergrim.borg.entities.Quote
import com.nethergrim.borg.entities.QuotesResponse
import com.nethergrim.borg.helpers.RetryWithDelay
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */
@RestController(value = "/borg/quotes")
open class QuotesController {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun handleGetQuotes(@RequestParam (value = "limit", defaultValue = "20") limit: String,
                        @RequestParam (value = "offset", defaultValue = "0") offset: String): QuotesResponse {

        return QuotesResponse(QuotesRepository.getRandomQuotes(Integer.parseInt(limit)) as List<Quote>, "result for limit $limit and offset $offset")
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/top")
    fun handleGetTopQuotes(@RequestParam (value = "limit", defaultValue = "20") limit: String,
                           @RequestParam (value = "offset", defaultValue = "0") offset: String): QuotesResponse {

        return QuotesResponse(parseBorgPageFromTop(1), "result for limit $limit and offset $offset")
    }


    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun handlePostQuotes(): QuotesResponse {
        getBorgPage(Int.MAX_VALUE)


        val indexes = ArrayList<Int>()
        for (i in lastBorgPageNumber downTo 1 step 1) {
            indexes.add(i)
        }

        val countDown = CountDownLatch(lastBorgPageNumber)

        Observable.from(indexes)
                .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(60)))
                .flatMap({ Observable.just(getBorgPage(it)) }, 60)
                .doOnNext { QuotesRepository.saveQuotes(it) }
                .retryWhen(RetryWithDelay(10, 300))
                .subscribe({ countDown.countDown() }, { countDown.countDown() })
        countDown.await()

        return QuotesResponse(emptyList<Quote>(), "fetched")
    }

}