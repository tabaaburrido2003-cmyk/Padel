package com.example.data

import java.security.MessageDigest

object SecurityUtils {
    // Salt to ensure dictionary attacks fail and PIN hashing is sturdy
    private const val PIN_SALT = "PadelMatch_Secure_Salt_2026_@!"

    /**
     * Hashes a user local passcode PIN using SHA-256 with a unique salt
     */
    fun hashPin(pin: String): String {
        val input = pin + PIN_SALT
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Verifies if a raw PIN matches the stored secure hash
     */
    fun verifyPin(pin: String, hash: String): Boolean {
        return hashPin(pin) == hash
    }
}
