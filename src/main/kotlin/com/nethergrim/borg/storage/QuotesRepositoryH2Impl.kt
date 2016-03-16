package com.nethergrim.borg.storage

import com.nethergrim.borg.entities.Quote
import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.jdbc.core.JdbcTemplate

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 * All rights reserved.
 */
@AutoConfigurationPackage
class QuotesRepositoryH2Impl : QuotesRepository {

    lateinit var jdbcTemplate: JdbcTemplate

    override fun clear() {
        jdbcTemplate.execute("DROP TABLE quotes IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE quotes(id BIGINT, quote VARCHAR(255), rating INT, date VARCHAR(255) )");

    }

    override fun saveQuotes(quotes: List<Quote>) {

    }
}