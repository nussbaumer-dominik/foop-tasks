package at.ac.tuwien.foop.plugins

import at.ac.tuwien.foop.common.serializerConfig
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureSocket() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(
            Json {
                serializersModule = serializerConfig
            }
        )
    }
}
