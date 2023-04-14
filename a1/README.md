# Aufgabe 1

## Architecture
### Frontend
The frontend is implemented using Kotlin Multiplatform Compose for Desktop. It draws the game on a canvas usign the received information from the server. 
User inputs are sent to the server and the current game state comes from the main server.

### Backend
The backend is a ktor-server. It's main responsibility is running the game loop. The game state is stored here and all user inputs are processed, which may alter the game state whcih will
then be sent back to all clients.

The communication between Server and Client will be handled via Websockets.

## How to run
TODO: Add How-to guide
