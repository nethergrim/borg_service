package com.nethergrim.borg.storage

import com.nethergrim.borg.entities.Quote
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */
@Component
interface QuotesRepository : CrudRepository<Quote, Long> {

}