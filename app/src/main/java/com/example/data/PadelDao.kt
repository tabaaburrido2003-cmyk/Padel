package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PadelDao {

    // User Operations
    @Query("SELECT * FROM users ORDER BY rankingPoints DESC")
    fun getAllUsersOrderedByRanking(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): Flow<User?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserByIdSuspend(id: Int): User?

    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    fun getCurrentUser(): Flow<User?>

    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUserSuspend(): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET isCurrentUser = 0")
    suspend fun clearCurrentUserFlag()

    @Delete
    suspend fun deleteUser(user: User)


    // Match Invitation Operations
    @Query("SELECT * FROM match_invitations ORDER BY timestamp DESC")
    fun getAllInvitations(): Flow<List<MatchInvitation>>

    @Query("SELECT * FROM match_invitations WHERE id = :id")
    fun getInvitationById(id: Int): Flow<MatchInvitation?>

    @Query("SELECT * FROM match_invitations WHERE id = :id")
    suspend fun getInvitationByIdSuspend(id: Int): MatchInvitation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvitation(invitation: MatchInvitation): Long

    @Update
    suspend fun updateInvitation(invitation: MatchInvitation)

    @Delete
    suspend fun deleteInvitation(invitation: MatchInvitation)
}
