package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE languageId = :languageId LIMIT 1")
    fun getProgressForLanguage(languageId: String): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: UserProgressEntity)

    @Query("UPDATE user_progress SET totalXp = totalXp + :xp, gems = gems + :gems WHERE languageId = :languageId")
    suspend fun addRewards(languageId: String, xp: Int, gems: Int)
}

@Dao
interface SnippetDao {
    @Query("SELECT * FROM saved_snippets WHERE languageId = :languageId ORDER BY createdAt DESC")
    fun getSnippetsForLanguage(languageId: String): Flow<List<SnippetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnippet(snippet: SnippetEntity)

    @Query("DELETE FROM saved_snippets WHERE id = :id")
    suspend fun deleteSnippet(id: Int)
}
