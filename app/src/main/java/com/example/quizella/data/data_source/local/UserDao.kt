package com.example.quizella.data.data_source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizella.domain.model.User

/**
 * Data Access Object for User entity.
 * Provides database operations for user authentication and management.
 */
@Dao
interface UserDao {

    /**
     * Inserts a new user into the database.
     * If a user with the same ID exists, it will be replaced.
     *
     * @param user The User object to insert.
     * @return The row ID of the newly inserted user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for.
     * @return The User object if found, null otherwise.
     */
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    /**
     * Authenticates a user by username and password hash.
     *
     * @param username The username to validate.
     * @param passwordHash The SHA-256 hashed password to compare.
     * @return The User object if credentials match, null otherwise.
     */
    @Query("SELECT * FROM users WHERE username = :username AND passwordHash = :passwordHash")
    suspend fun getUserByCredentials(username: String, passwordHash: String): User?
}