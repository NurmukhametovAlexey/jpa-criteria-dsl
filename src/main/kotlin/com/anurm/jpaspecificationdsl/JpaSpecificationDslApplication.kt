package com.anurm.jpaspecificationdsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class JpaSpecificationDslApplication {

}



fun main(args: Array<String>) {
    val context = runApplication<JpaSpecificationDslApplication>(*args)
}


