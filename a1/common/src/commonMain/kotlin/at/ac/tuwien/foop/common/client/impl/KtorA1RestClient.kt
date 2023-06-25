package at.ac.tuwien.foop.common.client.impl

import at.ac.tuwien.foop.common.client.A1RestClient
import at.ac.tuwien.foop.common.models.domain.rest.RegisterRequest
import at.ac.tuwien.foop.common.models.domain.rest.RegisterResponse
import at.ac.tuwien.foop.common.models.dtos.rest.RegisterResponseDto
import at.ac.tuwien.foop.common.models.exceptions.DuplicatedUsernameException
import at.ac.tuwien.foop.common.models.mapper.map
import at.ac.tuwien.foop.common.models.mapper.mapToDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.io.Closeable

internal class KtorA1RestClient(baseUrl: String) : A1RestClient, Closeable {
    private val client = HttpClient(CIO) {
        defaultRequest {
            url(baseUrl)
        }
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun join(registerRequest: RegisterRequest): Result<RegisterResponse> {
        val response: HttpResponse = client.post("sessions") {
            contentType(ContentType.Application.Json)
            setBody(registerRequest.mapToDto())
        }

        if (response.status == HttpStatusCode.Conflict) {
            return Result.failure(DuplicatedUsernameException())
        } else if (response.status != HttpStatusCode.OK) {
            return Result.failure(Exception("Could not login user [${response.status}]"))
        }

        return Result.success(response.body<RegisterResponseDto>().map())
    }

    override suspend fun start() {
        client.post("start")
    }

    override fun close() {
        client.close()
    }
}

fun getA1RestClient(baseUrl: String): A1RestClient =
    KtorA1RestClient(baseUrl)
