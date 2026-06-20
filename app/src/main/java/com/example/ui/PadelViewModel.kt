package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PadelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PadelRepository

    init {
        val database = PadelDatabase.getDatabase(application)
        repository = PadelRepository(database.padelDao())
        
        // Seed database if empty at startup
        viewModelScope.launch {
            repository.seedMockDataIfEmpty()
        }
    }

    // Reactive State Flows
    val currentUser: StateFlow<User?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allInvitations: StateFlow<List<MatchInvitation>> = repository.allInvitations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<User>> = repository.allUsersOrderedByRanking
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI States
    private val _isLocked = MutableStateFlow(true)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private val _verificationError = MutableStateFlow<String?>(null)
    val verificationError: StateFlow<String?> = _verificationError.asStateFlow()

    private val _rankingSearchQuery = MutableStateFlow("")
    val rankingSearchQuery: StateFlow<String> = _rankingSearchQuery.asStateFlow()

    private val _rankingLevelFilter = MutableStateFlow<String>("Todos")
    val rankingLevelFilter: StateFlow<String> = _rankingLevelFilter.asStateFlow()

    private val _feedLevelFilter = MutableStateFlow<String>("Todos")
    val feedLevelFilter: StateFlow<String> = _feedLevelFilter.asStateFlow()

    // Filtered lists
    val filteredUsers: StateFlow<List<User>> = combine(
        allUsers,
        _rankingSearchQuery,
        _rankingLevelFilter
    ) { users, query, levelFilter ->
        users.filter { user ->
            val matchesQuery = user.name.contains(query, ignoreCase = true)
            val matchesLevel = when (levelFilter) {
                "Todos" -> true
                "Principiante" -> user.level <= 2.5
                "Intermedio" -> user.level > 2.5 && user.level <= 4.0
                "Avanzado" -> user.level > 4.0 && user.level <= 5.5
                "Profesional" -> user.level > 5.5
                else -> true
            }
            matchesQuery && matchesLevel
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredInvitations: StateFlow<List<MatchInvitation>> = combine(
        allInvitations,
        _feedLevelFilter
    ) { invites, levelFilter ->
        invites.filter { invite ->
            when (levelFilter) {
                "Todos" -> true
                "Principiante" -> invite.levelRequired <= 2.5
                "Intermedio" -> invite.levelRequired > 2.5 && invite.levelRequired <= 4.0
                "Avanzado" -> invite.levelRequired > 4.0 && invite.levelRequired <= 5.5
                "Profesional" -> invite.levelRequired > 5.5
                else -> true
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // User Authentication & Registration Actions
    fun registerNewUser(
        name: String,
        email: String,
        phone: String,
        level: Double,
        position: String,
        pin: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (name.isBlank() || email.isBlank() || phone.isBlank() || pin.length < 4) {
                onError("Por favor, rellena todos los campos. El PIN debe ser de 4 dígitos.")
                return@launch
            }

            // Check if email already registered
            val existing = repository.getUserByEmail(email)
            if (existing != null) {
                onError("El email ya se encuentra registrado.")
                return@launch
            }

            val newUser = User(
                name = name,
                email = email,
                phone = phone,
                level = level,
                position = position,
                hashedPin = SecurityUtils.hashPin(pin),
                isCurrentUser = true,
                rankingPoints = 1000,
                avatarId = (1..10).random()
            )

            repository.insertUser(newUser)
            _isLocked.value = false
            onSuccess()
        }
    }

    fun verifyPinAndUnlock(pin: String): Boolean {
        val user = currentUser.value
        if (user == null) {
            _verificationError.value = "Regístrate antes de continuar."
            return false
        }

        val isValid = SecurityUtils.verifyPin(pin, user.hashedPin)
        if (isValid) {
            _isLocked.value = false
            _verificationError.value = null
        } else {
            _verificationError.value = "PIN incorrecto. Inténtalo de nuevo."
        }
        return isValid
    }

    fun lockApp() {
        _isLocked.value = true
        _verificationError.value = null
    }

    fun logout() {
        viewModelScope.launch {
            repository.logoutCurrentUser()
            _isLocked.value = true
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }


    // Filters Action
    fun updateRankingSearch(query: String) {
        _rankingSearchQuery.value = query
    }

    fun updateRankingLevelFilter(filter: String) {
        _rankingLevelFilter.value = filter
    }

    fun updateFeedLevelFilter(filter: String) {
        _feedLevelFilter.value = filter
    }


    // Match Invitation Operations
    fun createInvitation(
        clubName: String,
        date: String,
        time: String,
        playersNeeded: Int,
        levelRequired: Double,
        notes: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = currentUser.value
        if (user == null) {
            onError("Debes iniciar sesión para publicar un partido")
            return
        }

        if (clubName.isBlank() || date.isBlank() || time.isBlank()) {
            onError("Rellena la pista, fecha y hora del partido.")
            return
        }

        viewModelScope.launch {
            val newInvite = MatchInvitation(
                creatorId = user.id,
                creatorName = user.name,
                creatorLevel = user.level,
                clubName = clubName,
                date = date,
                time = time,
                playersNeeded = playersNeeded,
                levelRequired = levelRequired,
                notes = notes,
                status = "OPEN"
            )
            repository.insertInvitation(newInvite)
            onSuccess()
        }
    }

    fun deleteInvitation(invitation: MatchInvitation) {
        viewModelScope.launch {
            repository.deleteInvitation(invitation)
        }
    }

    fun toggleJoinMatch(invitation: MatchInvitation) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val playerIds = invitation.getPlayerIdsList().toMutableList()
            val playerNames = invitation.getPlayerNamesList().toMutableList()

            if (playerIds.contains(user.id)) {
                // User is leaving
                val index = playerIds.indexOf(user.id)
                if (index != -1) {
                    playerIds.removeAt(index)
                    if (index < playerNames.size) {
                        playerNames.removeAt(index)
                    }
                }
            } else {
                // User is joining
                if (invitation.isFull) return@launch // Full
                playerIds.add(user.id)
                playerNames.add(user.name)
            }

            val updatedIds = playerIds.joinToString(",")
            val updatedNames = playerNames.joinToString(",")
            val isNowFull = playerIds.size >= invitation.playersNeeded

            val updatedInvite = invitation.copy(
                joinedPlayerIds = updatedIds,
                joinedPlayerNames = updatedNames,
                status = if (isNowFull) "FILLED" else "OPEN"
            )

            repository.updateInvitation(updatedInvite)
        }
    }


    // Security & Privacy Phone number check
    /**
     * Determines whether user A is allowed to view user B's phone number.
     * To protect user data, a phone number is visible in this padel network only:
     * 1. If viewing themselves.
     * 2. If they are in the same active matchmaking invitation (playing together).
     * Otherwise, a redacted version of the phone ("+34 ******") is returned for security!
     */
    fun getSecurePhoneNumber(targetUser: User): String {
        val current = currentUser.value ?: return ""
        if (targetUser.id == current.id) {
            return targetUser.phone
        }

        // Check if current user shares any Match Invitation with targetUser
        val isSharingMatch = allInvitations.value.any { invite ->
            val isCreatorCurrent = invite.creatorId == current.id
            val isCreatorTarget = invite.creatorId == targetUser.id
            val isCurrentJoined = invite.getPlayerIdsList().contains(current.id)
            val isTargetJoined = invite.getPlayerIdsList().contains(targetUser.id)

            (isCreatorCurrent && isTargetJoined) || 
            (isCreatorTarget && isCurrentJoined) || 
            (isCurrentJoined && isTargetJoined)
        }

        if (isSharingMatch) {
            return targetUser.phone
        }

        // Phone is obfuscated for data security!
        val rawPhone = targetUser.phone
        if (rawPhone.length > 5) {
            return rawPhone.take(4) + " ••• ••• " + rawPhone.takeLast(2)
        }
        return "• • • • •"
    }
}
