package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val languageId: String,
    val completedLessonIds: String = "", // Comma-separated lesson IDs
    val totalXp: Int = 0,
    val streak: Int = 1,
    val gems: Int = 100,
    val lastActiveDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_snippets")
data class SnippetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val languageId: String,
    val title: String,
    val code: String,
    val createdAt: Long = System.currentTimeMillis()
)
