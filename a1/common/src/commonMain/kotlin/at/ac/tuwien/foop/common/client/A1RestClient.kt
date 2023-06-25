package at.ac.tuwien.foop.common.client

import at.ac.tuwien.foop.common.models.domain.rest.RegisterRequest
import at.ac.tuwien.foop.common.models.domain.rest.RegisterResponse
import java.io.Closeable

interface A1RestClient : Closeable {
    suspend fun join(registerRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun start()
}
