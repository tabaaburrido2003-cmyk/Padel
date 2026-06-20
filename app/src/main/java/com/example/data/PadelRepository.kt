package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class PadelRepository(private val dao: PadelDao) {
    val allUsersOrderedByRanking: Flow<List<User>> = dao.getAllUsersOrderedByRanking()
    val currentUser: Flow<User?> = dao.getCurrentUser()
    val allInvitations: Flow<List<MatchInvitation>> = dao.getAllInvitations()

    fun getUserById(id: Int): Flow<User?> = dao.getUserById(id)

    suspend fun getUserByIdSuspend(id: Int): User? = dao.getUserByIdSuspend(id)

    suspend fun getUserByEmail(email: String): User? = dao.getUserByEmail(email)

    suspend fun insertUser(user: User): Long {
        return dao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }

    suspend fun loginUser(user: User) {
        dao.clearCurrentUserFlag()
        dao.updateUser(user.copy(isCurrentUser = true))
    }

    suspend fun logoutCurrentUser() {
        dao.clearCurrentUserFlag()
    }

    fun getInvitationById(id: Int): Flow<MatchInvitation?> = dao.getInvitationById(id)

    suspend fun insertInvitation(invitation: MatchInvitation): Long {
        return dao.insertInvitation(invitation)
    }

    suspend fun updateInvitation(invitation: MatchInvitation) {
        dao.updateInvitation(invitation)
    }

    suspend fun deleteInvitation(invitation: MatchInvitation) {
        dao.deleteInvitation(invitation)
    }

    /**
     * Seeds mock players and match invitations to provide an immersive first-launch experience.
     */
    suspend fun seedMockDataIfEmpty() {
        val users = allUsersOrderedByRanking.firstOrNull() ?: emptyList()
        // If we only have 0 or 1 user (the player), seed mock players to make ranking and matchmaking robust
        if (users.isEmpty() || (users.size == 1 && users[0].isCurrentUser)) {
            // Seed professional and local mock padel players
            val mockPlayers = listOf(
                User(
                    id = 101,
                    name = "Arturo Coello",
                    level = 6.8,
                    position = "Drive",
                    phone = "+34 600 111 222",
                    email = "coello@padel.com",
                    hashedPin = SecurityUtils.hashPin("1111"),
                    rankingPoints = 2250,
                    matchesPlayed = 50,
                    matchesWon = 48,
                    matchesLost = 2,
                    avatarId = 1
                ),
                User(
                    id = 102,
                    name = "Alejandro Galán",
                    level = 6.5,
                    position = "Revés",
                    phone = "+34 600 333 444",
                    email = "galan@padel.com",
                    hashedPin = SecurityUtils.hashPin("2222"),
                    rankingPoints = 2100,
                    matchesPlayed = 50,
                    matchesWon = 45,
                    matchesLost = 5,
                    avatarId = 2
                ),
                User(
                    id = 103,
                    name = "Juan Lebrón",
                    level = 6.5,
                    position = "Drive",
                    phone = "+34 600 555 666",
                    email = "lebron@padel.com",
                    hashedPin = SecurityUtils.hashPin("3333"),
                    rankingPoints = 2050,
                    matchesPlayed = 49,
                    matchesWon = 42,
                    matchesLost = 7,
                    avatarId = 3
                ),
                User(
                    id = 104,
                    name = "Bea González",
                    level = 6.0,
                    position = "Revés",
                    phone = "+34 600 777 888",
                    email = "bea@padel.com",
                    hashedPin = SecurityUtils.hashPin("4444"),
                    rankingPoints = 1950,
                    matchesPlayed = 42,
                    matchesWon = 38,
                    matchesLost = 4,
                    avatarId = 4
                ),
                User(
                    id = 105,
                    name = "Paquito Navarro",
                    level = 6.0,
                    position = "Revés",
                    phone = "+34 600 999 000",
                    email = "paquito@padel.com",
                    hashedPin = SecurityUtils.hashPin("5555"),
                    rankingPoints = 1900,
                    matchesPlayed = 44,
                    matchesWon = 35,
                    matchesLost = 9,
                    avatarId = 5
                ),
                User(
                    id = 106,
                    name = "Marta Marrero",
                    level = 5.5,
                    position = "Revés",
                    phone = "+34 611 222 333",
                    email = "marta@padel.com",
                    hashedPin = SecurityUtils.hashPin("6666"),
                    rankingPoints = 1750,
                    matchesPlayed = 38,
                    matchesWon = 30,
                    matchesLost = 8,
                    avatarId = 6
                ),
                User(
                    id = 107,
                    name = "Delfi Brea",
                    level = 5.5,
                    position = "Drive",
                    phone = "+34 611 444 555",
                    email = "delfi@padel.com",
                    hashedPin = SecurityUtils.hashPin("7777"),
                    rankingPoints = 1700,
                    matchesPlayed = 35,
                    matchesWon = 28,
                    matchesLost = 7,
                    avatarId = 7
                ),
                User(
                    id = 108,
                    name = "Carolina Navarro",
                    level = 5.0,
                    position = "Drive",
                    phone = "+34 611 666 777",
                    email = "caro@padel.com",
                    hashedPin = SecurityUtils.hashPin("8888"),
                    rankingPoints = 1600,
                    matchesPlayed = 35,
                    matchesWon = 25,
                    matchesLost = 10,
                    avatarId = 8
                ),
                User(
                    id = 109,
                    name = "Carlos Pérez (Amateur)",
                    level = 3.5,
                    position = "Ambos",
                    phone = "+34 622 111 000",
                    email = "carlosp@padelmail.com",
                    hashedPin = SecurityUtils.hashPin("9999"),
                    rankingPoints = 1120,
                    matchesPlayed = 15,
                    matchesWon = 8,
                    matchesLost = 7,
                    avatarId = 9
                ),
                User(
                    id = 110,
                    name = "Lucía Gómez (Amateur)",
                    level = 3.0,
                    position = "Drive",
                    phone = "+34 622 222 111",
                    email = "luciag@padelmail.com",
                    hashedPin = SecurityUtils.hashPin("0000"),
                    rankingPoints = 1040,
                    matchesPlayed = 10,
                    matchesWon = 5,
                    matchesLost = 5,
                    avatarId = 10
                )
            )

            for (player in mockPlayers) {
                dao.insertUser(player)
            }
        }

        // Seed some active match invitations if empty
        val currentInvitations = allInvitations.firstOrNull() ?: emptyList()
        if (currentInvitations.isEmpty()) {
            val mockInvitations = listOf(
                MatchInvitation(
                    id = 1,
                    creatorId = 105, // Paquito Navarro
                    creatorName = "Paquito Navarro",
                    creatorLevel = 6.0,
                    clubName = "Pádel Club El Hangar",
                    date = "Mañana",
                    time = "19:00",
                    playersNeeded = 1,
                    joinedPlayerIds = "109", // Carlos joined
                    joinedPlayerNames = "Carlos Pérez (Amateur)",
                    levelRequired = 4.0,
                    notes = "¡Falta un revés agresivo para cerrar un 2v2 de nivel alto! Pista reservada en zona centro.",
                    status = "OPEN"
                ),
                MatchInvitation(
                    id = 2,
                    creatorId = 101, // Arturo Coello
                    creatorName = "Arturo Coello",
                    creatorLevel = 6.8,
                    clubName = "Hangar Arena Premium",
                    date = "Pasado Mañana",
                    time = "21:00",
                    playersNeeded = 2,
                    joinedPlayerIds = "",
                    joinedPlayerNames = "",
                    levelRequired = 5.5,
                    notes = "Entrenamiento de golpes rápidos y bandejas. Solo jugadores avanzados por favor, buscamos intensidad.",
                    status = "OPEN"
                ),
                MatchInvitation(
                    id = 3,
                    creatorId = 110, // Lucía Gómez
                    creatorName = "Lucía Gómez",
                    creatorLevel = 3.0,
                    clubName = "PoliDeportivo Municipal",
                    date = "Este Sábado",
                    time = "10:30",
                    playersNeeded = 3,
                    joinedPlayerIds = "",
                    joinedPlayerNames = "",
                    levelRequired = 3.0,
                    notes = "¡Jugamos por diversión! Buscamos pasar un buen rato y hacer un poco de ejercicio.",
                    status = "OPEN"
                )
            )

            for (invite in mockInvitations) {
                dao.insertInvitation(invite)
            }
        }
    }
}
