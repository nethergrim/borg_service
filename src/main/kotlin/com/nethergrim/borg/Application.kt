package com.nethergrim.borg

import com.nethergrim.borg.restful.QuotesController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}


@Autowired
var quotesController: QuotesController? = null







