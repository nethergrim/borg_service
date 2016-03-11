package com.nethergrim.borg.helpers

import rx.Observable
import rx.functions.Func1
import java.util.concurrent.TimeUnit

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * *         All rights reserved.
 */
class RetryWithDelay(private val _maxRetries: Int, private val _retryDelayMillis: Int) : Func1<Observable<out Throwable>, Observable<*>> {
    private var _retryCount: Int = 0

    init {
        _retryCount = 0
    }

    override fun call(attempts: Observable<out Throwable>): Observable<*> {

        return attempts.flatMap(Func1<kotlin.Throwable, rx.Observable<*>> { throwable ->
            if (++_retryCount < _maxRetries) {
                // When this Observable calls onNext, the original
                // Observable will be retried (i.e. re-subscribed).

                return@Func1 Observable.timer((_retryCount * _retryDelayMillis).toLong(), TimeUnit.MILLISECONDS)

            }

            // Max retries hit. Just pass the error along.
            Observable.error<Any>(throwable)
        })
    }
}
