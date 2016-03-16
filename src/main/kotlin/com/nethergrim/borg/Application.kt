package com.nethergrim.borg

import com.nethergrim.borg.storage.QuotesRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class Application {

    @Bean
    open fun init(repository: QuotesRepository) = CommandLineRunner {
        //        repository.save(Customer("Jack", "Bauer"))
        //        repository.save(Customer("Chloe", "O'Brian"))
        //        repository.save(Customer("Kim", "Bauer"))
        //        repository.save(Customer("David", "Palmer"))
        //        repository.save(Customer("Michelle", "Dessler"))

    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}









