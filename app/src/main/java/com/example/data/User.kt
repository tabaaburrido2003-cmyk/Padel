package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val level: Double, // 1.0 to 7.0 standard padel rating
    val position: String, // "Drive", "Revés", "Ambos"
    val phone: String,
    val email: String,
    val hashedPin: String, // Security: hashed local passcode PIN
    val isCurrentUser: Boolean = false,
    val rankingPoints: Int = 1000,
    val matchesPlayed: Int = 0,
    val matchesWon: Int = 0,
    val matchesLost: Int = 0,
    val avatarId: Int = 0
) {
    val levelLabel: String
        get() = when {
            level <= 2.5 -> "Principiante"
            level <= 4.0 -> "Intermedio"
            level <= 5.5 -> "Avanzado"
            else -> "Profesional"
        }
}
