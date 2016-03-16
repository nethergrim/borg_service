package com.nethergrim.borg

import com.nethergrim.borg.data.FetchAllQuotesTask
import com.nethergrim.borg.storage.QuotesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootApplication
open class Application : CommandLineRunner {

    @Autowired
    lateinit open var jdbcTemplate: JdbcTemplate

    override fun run(vararg p0: String?) {
        QuotesRepository.instance.jdbcTemplate = this.jdbcTemplate
        QuotesRepository.instance.clear()
        FetchAllQuotesTask.instance.fetchAllQuotes()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}









