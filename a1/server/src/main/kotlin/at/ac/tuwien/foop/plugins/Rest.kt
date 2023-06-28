package at.ac.tuwien.foop.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureRest() {
    install(ContentNegotiation) {
        json()
    }
}
