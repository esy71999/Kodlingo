package com.example.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val progress: Int,
    val maxProgress: Int,
    val gemReward: Int
)

data class DailyQuest(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val isCompleted: Boolean,
    val current: Int,
    val target: Int
)
