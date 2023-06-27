package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.game.Game
import at.ac.tuwien.foop.common.models.dtos.rest.RegisterRequestDto
import at.ac.tuwien.foop.common.models.exceptions.DuplicatedUsernameException
import at.ac.tuwien.foop.common.models.mapper.map
import at.ac.tuwien.foop.common.models.mapper.mapToDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.restEndpoint(game: Game) {
    routing {
        post("sessions") {
            println("Received create session request")
            val request = call.receive<RegisterRequestDto>().map()

            if(request.username.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Username cannot be empty",
                )
            }

            val response = game.registerUser(request)
            if (response.isFailure && response.exceptionOrNull() is DuplicatedUsernameException) {
                call.respond(HttpStatusCode.Conflict, "Username already exists")
            } else if (response.isFailure) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    response.exceptionOrNull()?.message ?: "An error occurred",
                )
            }

            call.respond(response.getOrThrow().mapToDto())
        }

        post("start") {
            println("Received start game request")
            game.startGame()
        }
    }
}
