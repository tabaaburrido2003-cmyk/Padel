package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_invitations")
data class MatchInvitation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val creatorId: Int,
    val creatorName: String,
    val creatorLevel: Double,
    val clubName: String,
    val date: String,
    val time: String,
    val playersNeeded: Int, // e.g. 1 if "nos falta uno", 2, or 3
    val joinedPlayerIds: String = "", // Comma-separated user IDs who joined
    val joinedPlayerNames: String = "", // Comma-separated names who joined
    val levelRequired: Double,
    val notes: String = "",
    val status: String = "OPEN", // "OPEN", "FILLED"
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getPlayerIdsList(): List<Int> {
        if (joinedPlayerIds.isBlank()) return emptyList()
        return joinedPlayerIds.split(",").mapNotNull { it.toIntOrNull() }
    }

    fun getPlayerNamesList(): List<String> {
        if (joinedPlayerNames.isBlank()) return emptyList()
        return joinedPlayerNames.split(",")
    }

    val isFull: Boolean
        get() = getPlayerIdsList().size >= playersNeeded
}
