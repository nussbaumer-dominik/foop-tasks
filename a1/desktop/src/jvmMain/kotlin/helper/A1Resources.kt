package helper

import at.ac.tuwien.foop.common.client.A1RestClient
import at.ac.tuwien.foop.common.client.A1SocketClient
import at.ac.tuwien.foop.common.client.impl.SocketMessageListener
import at.ac.tuwien.foop.common.client.impl.getA1RestClient
import at.ac.tuwien.foop.common.client.impl.getA1SocketClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

fun withResources(
    restUrl: String,
    socketUrl: String,
    onSocketMessage: SocketMessageListener,
    callback: CoroutineScope.(
        restClient: A1RestClient,
        socketClient: A1SocketClient,
    ) -> Unit,
) {
    val applicationScope = CoroutineScope(Dispatchers.Default + Job())
    try {
        getA1RestClient(baseUrl = restUrl).use { restClient ->
            getA1SocketClient(
                baseUrl = socketUrl,
                listener = onSocketMessage,
            ).use { socketClient ->
                applicationScope.callback(restClient, socketClient)
            }
        }
    } finally {
        applicationScope.cancel()
    }
}
