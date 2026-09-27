package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.CurriculumData
import com.example.data.local.AppDatabase
import com.example.data.local.SnippetEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.Achievement
import com.example.data.model.CodingLanguage
import com.example.data.model.DailyQuest
import com.example.data.model.LeagueTier
import com.example.data.model.LeagueUser
import com.example.data.model.Lesson
import com.example.data.model.SupportedLanguages
import com.example.data.model.UnitData
import com.example.data.repository.CodeLingoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab {
    LEARN,       // 🎓 Yol Haritası
    ARENA,       // ⚔️ Hata Avı & Görevler
    LEADERBOARD, // 🏆 Ligler
    SANDBOX,     // 💻 Kod Alanı
    PROFILE      // 👤 Profil & Rozetler
}

@OptIn(ExperimentalCoroutinesApi::class)
class CodeLingoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CodeLingoRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CodeLingoRepository(db.userProgressDao(), db.snippetDao())
    }

    // Active Selected Language
    private val _currentLanguage = MutableStateFlow(SupportedLanguages.getById("gdscript"))
    val currentLanguage: StateFlow<CodingLanguage> = _currentLanguage.asStateFlow()

    // Navigation Tab
    private val _activeTab = MutableStateFlow(NavigationTab.LEARN)
    val activeTab: StateFlow<NavigationTab> = _activeTab.asStateFlow()

    // Currently Playing Lesson (null = browsing main screens)
    private val _activeLesson = MutableStateFlow<Lesson?>(null)
    val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

    // Modals / Dialogs visibility
    var showLanguagePicker = MutableStateFlow(false)
    var showNoHeartsDialog = MutableStateFlow(false)
    var showDebApkDialog = MutableStateFlow(false)

    // Current language progress observed reactively from Room
    val userProgress: StateFlow<UserProgressEntity> = _currentLanguage
        .flatMapLatest { lang ->
            repository.getProgressForLanguage(lang.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProgressEntity(
                languageId = "gdscript",
                completedLessonIds = "",
                totalXp = 120,
                streak = 3,
                gems = 150
            )
        )

    // Saved snippets for current language
    val savedSnippets: StateFlow<List<SnippetEntity>> = _currentLanguage
        .flatMapLatest { lang ->
            repository.getSnippetsForLanguage(lang.id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectLanguage(language: CodingLanguage) {
        _currentLanguage.value = language
    }

    fun selectTab(tab: NavigationTab) {
        _activeTab.value = tab
    }

    fun startLesson(lesson: Lesson) {
        _activeLesson.value = lesson
    }

    fun finishLesson(xpEarned: Int, gemsEarned: Int) {
        val currentLesson = _activeLesson.value ?: return
        val currentLangId = _currentLanguage.value.id

        viewModelScope.launch {
            repository.completeLesson(currentLangId, currentLesson.id, xpEarned, gemsEarned)
            _activeLesson.value = null
        }
    }

    fun cancelLesson() {
        _activeLesson.value = null
    }

    fun getUnitsForCurrentLanguage(): List<UnitData> {
        return CurriculumData.getUnitsForLanguage(_currentLanguage.value.id)
    }

    fun getCompletedLessonIds(): Set<String> {
        return userProgress.value.completedLessonIds
            .split(",")
            .filter { it.isNotBlank() }
            .toSet()
    }

    fun getLeaderboard(): List<LeagueUser> {
        return repository.getLeaderboard(userProgress.value.totalXp)
    }

    fun getAchievements(): List<Achievement> {
        val completedCount = getCompletedLessonIds().size
        return repository.getAchievements(
            completedLessonsCount = completedCount,
            totalXp = userProgress.value.totalXp,
            streak = userProgress.value.streak
        )
    }

    fun getDailyQuests(): List<DailyQuest> {
        val completedCount = getCompletedLessonIds().size
        return repository.getDailyQuests(
            completedToday = completedCount.coerceAtMost(2),
            xpToday = (userProgress.value.totalXp % 100)
        )
    }

    fun saveSnippet(title: String, code: String) {
        viewModelScope.launch {
            repository.saveSnippet(_currentLanguage.value.id, title, code)
        }
    }

    fun deleteSnippet(id: Int) {
        viewModelScope.launch {
            repository.deleteSnippet(id)
        }
    }
}
