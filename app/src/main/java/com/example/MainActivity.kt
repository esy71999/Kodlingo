package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LeagueTier
import com.example.ui.components.DebApkInfoDialog
import com.example.ui.components.DuoBottomNavBar
import com.example.ui.components.DuoNavRail
import com.example.ui.components.DuoTopBar
import com.example.ui.components.NoHeartsDialog
import com.example.ui.screens.arena.ArenaScreen
import com.example.ui.screens.leaderboard.LeaderboardScreen
import com.example.ui.screens.learn.LanguagePickerModal
import com.example.ui.screens.learn.LearnScreen
import com.example.ui.screens.lesson.LessonScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.sandbox.CodeSandboxScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CodeLingoViewModel
import com.example.ui.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {

    private val viewModel: CodeLingoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: CodeLingoViewModel) {
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val userProgress by viewModel.userProgress.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val activeLesson by viewModel.activeLesson.collectAsStateWithLifecycle()
    val savedSnippets by viewModel.savedSnippets.collectAsStateWithLifecycle()

    val showLanguagePicker by viewModel.showLanguagePicker.collectAsStateWithLifecycle()
    val showNoHeartsDialog by viewModel.showNoHeartsDialog.collectAsStateWithLifecycle()
    val showDebApkDialog by viewModel.showDebApkDialog.collectAsStateWithLifecycle()

    // If an interactive lesson is currently active, show LessonScreen with its own back handler
    if (activeLesson != null) {
        BackHandler {
            viewModel.cancelLesson()
        }

        LessonScreen(
            lesson = activeLesson!!,
            onFinish = { xp, gems ->
                viewModel.finishLesson(xp, gems)
            },
            onCancel = {
                viewModel.cancelLesson()
            }
        )
        return
    }

    // Secondary screen back handler: if on another tab, back press returns to LEARN tab
    if (activeTab != NavigationTab.LEARN) {
        BackHandler {
            viewModel.selectTab(NavigationTab.LEARN)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Row(modifier = Modifier.fillMaxSize()) {
            if (isWideScreen) {
                DuoNavRail(
                    selectedTab = activeTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }

            Scaffold(
                modifier = Modifier.weight(1f),
                topBar = {
                    DuoTopBar(
                        currentLanguage = currentLanguage,
                        streak = userProgress.streak,
                        gems = userProgress.gems,
                        xp = userProgress.totalXp,
                        onLanguageClick = { viewModel.showLanguagePicker.value = true },
                        onHeartsClick = { viewModel.showNoHeartsDialog.value = true }
                    )
                },
                bottomBar = {
                    if (!isWideScreen) {
                        DuoBottomNavBar(
                            selectedTab = activeTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = if (isWideScreen) 860.dp else Dp.Unspecified)
                    ) {
                        AnimatedContent(
                            targetState = activeTab,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "tab_animation"
                        ) { targetTab ->
                            when (targetTab) {
                                NavigationTab.LEARN -> {
                                    LearnScreen(
                                        currentLanguage = currentLanguage,
                                        units = viewModel.getUnitsForCurrentLanguage(),
                                        completedLessonIds = viewModel.getCompletedLessonIds(),
                                        onSelectLesson = { lesson ->
                                            viewModel.startLesson(lesson)
                                        }
                                    )
                                }

                                NavigationTab.ARENA -> {
                                    ArenaScreen(
                                        dailyQuests = viewModel.getDailyQuests(),
                                        onStartSpeedDrill = {
                                            // Start the first lesson as practice drill
                                            val units = viewModel.getUnitsForCurrentLanguage()
                                            val firstLesson = units.firstOrNull()?.lessons?.firstOrNull()
                                            if (firstLesson != null) {
                                                viewModel.startLesson(firstLesson)
                                            }
                                        }
                                    )
                                }

                                NavigationTab.LEADERBOARD -> {
                                    LeaderboardScreen(
                                        currentTier = LeagueTier.GOLD,
                                        users = viewModel.getLeaderboard()
                                    )
                                }

                                NavigationTab.SANDBOX -> {
                                    CodeSandboxScreen(
                                        currentLanguage = currentLanguage,
                                        savedSnippets = savedSnippets,
                                        onSaveSnippet = { title, code ->
                                            viewModel.saveSnippet(title, code)
                                        },
                                        onDeleteSnippet = { id ->
                                            viewModel.deleteSnippet(id)
                                        }
                                    )
                                }

                                NavigationTab.PROFILE -> {
                                    ProfileScreen(
                                        streak = userProgress.streak,
                                        totalXp = userProgress.totalXp,
                                        gems = userProgress.gems,
                                        completedLessonsCount = viewModel.getCompletedLessonIds().size,
                                        achievements = viewModel.getAchievements(),
                                        onOpenDebApkDialog = { viewModel.showDebApkDialog.value = true },
                                        onOpenHeartsDialog = { viewModel.showNoHeartsDialog.value = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modals
    if (showLanguagePicker) {
        LanguagePickerModal(
            currentLanguageId = currentLanguage.id,
            onLanguageSelected = { lang ->
                viewModel.selectLanguage(lang)
            },
            onDismiss = { viewModel.showLanguagePicker.value = false }
        )
    }

    if (showNoHeartsDialog) {
        NoHeartsDialog(
            onDismiss = { viewModel.showNoHeartsDialog.value = false }
        )
    }

    if (showDebApkDialog) {
        DebApkInfoDialog(
            onDismiss = { viewModel.showDebApkDialog.value = false }
        )
    }
}
