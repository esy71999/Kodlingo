package com.example.data.repository

import com.example.data.local.SnippetDao
import com.example.data.local.SnippetEntity
import com.example.data.local.UserProgressDao
import com.example.data.local.UserProgressEntity
import com.example.data.model.Achievement
import com.example.data.model.DailyQuest
import com.example.data.model.LeagueTier
import com.example.data.model.LeagueUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class CodeLingoRepository(
    private val userProgressDao: UserProgressDao,
    private val snippetDao: SnippetDao
) {
    fun getProgressForLanguage(languageId: String): Flow<UserProgressEntity> {
        return userProgressDao.getProgressForLanguage(languageId).map { entity ->
            entity ?: UserProgressEntity(
                languageId = languageId,
                completedLessonIds = "",
                totalXp = 0,
                streak = 3,
                gems = 150
            )
        }
    }

    suspend fun completeLesson(languageId: String, lessonId: String, xp: Int, gems: Int) {
        val current = userProgressDao.getProgressForLanguage(languageId).firstOrNull()
            ?: UserProgressEntity(languageId = languageId)

        val completedSet = current.completedLessonIds.split(",")
            .filter { it.isNotBlank() }
            .toMutableSet()
        val isFirstTime = completedSet.add(lessonId)

        val newXp = current.totalXp + xp
        val newGems = current.gems + gems
        val newCompletedString = completedSet.joinToString(",")

        userProgressDao.insertOrUpdate(
            current.copy(
                completedLessonIds = newCompletedString,
                totalXp = newXp,
                gems = newGems,
                streak = if (isFirstTime && current.streak == 0) 1 else current.streak,
                lastActiveDate = System.currentTimeMillis()
            )
        )
    }

    fun getSnippetsForLanguage(languageId: String): Flow<List<SnippetEntity>> {
        return snippetDao.getSnippetsForLanguage(languageId)
    }

    suspend fun saveSnippet(languageId: String, title: String, code: String) {
        snippetDao.insertSnippet(
            SnippetEntity(
                languageId = languageId,
                title = title,
                code = code
            )
        )
    }

    suspend fun deleteSnippet(id: Int) {
        snippetDao.deleteSnippet(id)
    }

    fun getLeaderboard(currentUserXp: Int): List<LeagueUser> {
        val rivals = listOf(
            LeagueUser(1, "Emre (Godot Guru)", 840, "🦊", false, "🔥 14 Gün"),
            LeagueUser(2, "Selin (Roblox Master)", 720, "🦄", false, "⚡ Hızlı"),
            LeagueUser(3, "Sen (Kod Şampiyonu)", currentUserXp.coerceAtLeast(310), "🦉", true, "👑 Liderlik"),
            LeagueUser(4, "Mert (Unity Dev)", 290, "🎮", false),
            LeagueUser(5, "Can (Pygame Pro)", 265, "🐍", false),
            LeagueUser(6, "Bora (Unreal C++)", 210, "⚡", false),
            LeagueUser(7, "Zeynep (Phaser JS)", 185, "💻", false),
            LeagueUser(8, "Kaan (Rust Bevy)", 140, "🦀", false),
            LeagueUser(9, "Ayşe (Shader Wizard)", 110, "✨", false),
            LeagueUser(10, "Deniz (Game Designer)", 95, "🎨", false)
        ).sortedByDescending { it.xp }

        return rivals.mapIndexed { index, user ->
            user.copy(rank = index + 1)
        }
    }

    fun getAchievements(completedLessonsCount: Int, totalXp: Int, streak: Int): List<Achievement> {
        return listOf(
            Achievement(
                id = "ach_first_code",
                title = "İlk Satır Kod!",
                description = "İlk interaktif oyun kodlama dersini tamamla.",
                iconEmoji = "🎯",
                isUnlocked = completedLessonsCount >= 1,
                progress = completedLessonsCount.coerceAtMost(1),
                maxProgress = 1,
                gemReward = 20
            ),
            Achievement(
                id = "ach_streak_3",
                title = "Ateşli Kodlayıcı",
                description = "3 günlük kodlama serisine ulaş.",
                iconEmoji = "🔥",
                isUnlocked = streak >= 3,
                progress = streak.coerceAtMost(3),
                maxProgress = 3,
                gemReward = 50
            ),
            Achievement(
                id = "ach_xp_300",
                title = "Deneyim Canavarı",
                description = "Toplamda 300 XP topla.",
                iconEmoji = "⚡",
                isUnlocked = totalXp >= 300,
                progress = totalXp.coerceAtMost(300),
                maxProgress = 300,
                gemReward = 60
            ),
            Achievement(
                id = "ach_game_master",
                title = "Oyun Mimarı",
                description = "Farklı oyun dillerinde 5 ders bitir.",
                iconEmoji = "🏆",
                isUnlocked = completedLessonsCount >= 5,
                progress = completedLessonsCount.coerceAtMost(5),
                maxProgress = 5,
                gemReward = 100
            )
        )
    }

    fun getDailyQuests(completedToday: Int, xpToday: Int): List<DailyQuest> {
        return listOf(
            DailyQuest(
                id = "quest_1",
                title = "2 Ders Tamamla",
                description = "Herhangi bir oyun dilinde pratik yap",
                xpReward = 30,
                isCompleted = completedToday >= 2,
                current = completedToday.coerceAtMost(2),
                target = 2
            ),
            DailyQuest(
                id = "quest_2",
                title = "50 XP Topla",
                description = "Soru çözerek deneyim puanı kazan",
                xpReward = 40,
                isCompleted = xpToday >= 50,
                current = xpToday.coerceAtMost(50),
                target = 50
            ),
            DailyQuest(
                id = "quest_3",
                title = "Hatasız Seri Yap",
                description = "Bir dersi ilk denemede bitir",
                xpReward = 50,
                isCompleted = completedToday >= 1,
                current = completedToday.coerceAtMost(1),
                target = 1
            )
        )
    }
}
