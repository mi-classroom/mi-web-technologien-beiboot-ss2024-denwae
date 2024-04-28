package de.thkoeln.web2024

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class Web2024Application

fun main(args: Array<String>) {
	runApplication<Web2024Application>(*args)
}
