package com.example.ui.screens.lesson

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Exercise
import com.example.data.model.ExerciseType
import com.example.data.model.Lesson
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.components.DuoTextButton
import com.example.ui.theme.CodeBg
import com.example.ui.theme.CodeFg
import com.example.ui.theme.CodeKeyword
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoGreenLight
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoRedLight
import com.example.ui.theme.DuoTextDark
import com.example.ui.theme.DuoYellow
import com.example.ui.theme.DuoYellowLight

enum class AnswerStatus {
    UNANSWERED,
    CORRECT,
    INCORRECT
}

@Composable
fun LessonScreen(
    lesson: Lesson,
    onFinish: (xpEarned: Int, gemsEarned: Int) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dynamic queue of exercises: missed exercises get pushed to the end so user masters them!
    val exerciseQueue = remember(lesson) { mutableStateListOf<Exercise>().apply { addAll(lesson.exercises) } }
    var currentQueueIndex by remember { mutableIntStateOf(0) }
    val initialTotal = remember(lesson) { lesson.exercises.size }
    var completedExercisesCount by remember { mutableIntStateOf(0) }

    var isLessonCompleted by remember { mutableStateOf(false) }

    // Current exercise state
    val currentExercise = if (currentQueueIndex < exerciseQueue.size) exerciseQueue[currentQueueIndex] else null
    var answerStatus by remember { mutableStateOf(AnswerStatus.UNANSWERED) }

    // User selection states for different exercise types:
    val selectedTokens = remember { mutableStateListOf<String>() }
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var selectedBugLineIndex by remember { mutableIntStateOf(-1) }

    // Matching pairs states:
    var selectedLeftIndex by remember { mutableIntStateOf(-1) }
    val matchedPairs = remember { mutableStateListOf<Pair<Int, Int>>() }

    // Reset inputs when moving to next exercise
    LaunchedEffect(currentQueueIndex) {
        answerStatus = AnswerStatus.UNANSWERED
        selectedTokens.clear()
        selectedOptionIndex = -1
        selectedBugLineIndex = -1
        selectedLeftIndex = -1
        matchedPairs.clear()
    }

    if (isLessonCompleted || currentExercise == null) {
        LessonVictoryView(
            lesson = lesson,
            onContinue = {
                onFinish(lesson.xpReward, lesson.gemReward)
            }
        )
        return
    }

    val progressFraction = (completedExercisesCount.toFloat() / initialTotal.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "progress")

    // Determine if user has selected enough to enable "KONTROL ET"
    val isCheckEnabled = when (currentExercise.type) {
        ExerciseType.WORD_BANK -> selectedTokens.isNotEmpty()
        ExerciseType.FILL_IN_BLANK -> selectedOptionIndex != -1
        ExerciseType.MULTIPLE_CHOICE -> selectedOptionIndex != -1
        ExerciseType.BUG_HUNT -> selectedBugLineIndex != -1
        ExerciseType.MATCH_PAIRS -> matchedPairs.size == currentExercise.pairLeft.size
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Navigation & Progress Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Çıkış",
                    tint = DuoTextDark
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Duolingo smooth green progress bar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE5E5E5))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DuoGreen)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Infinite Lives Indicator (User requirement: Can sistemi olmasın)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(DuoGreen.copy(alpha = 0.1f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = DuoRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.AllInclusive,
                    contentDescription = null,
                    tint = DuoGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Main Exercise Body (Adaptive Side-by-Side in Landscape)
        androidx.compose.foundation.layout.BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            val isWide = maxWidth >= 600.dp

            if (isWide) {
                // Landscape / Desktop Dual-Pane Layout
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Left Pane: Question & Code Block
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = currentExercise.prompt,
                            fontWeight = FontWeight.Black,
                            fontSize = 19.sp,
                            color = DuoTextDark,
                            lineHeight = 24.sp
                        )

                        if (currentExercise.codeContext != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CodeBg)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = currentExercise.codeContext,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = CodeFg,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    // Right Pane: Interaction Area
                    Column(
                        modifier = Modifier
                            .weight(1.2f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        when (currentExercise.type) {
                            ExerciseType.WORD_BANK -> {
                                WordBankExerciseView(
                                    exercise = currentExercise,
                                    selectedTokens = selectedTokens,
                                    onAddToken = { selectedTokens.add(it) },
                                    onRemoveToken = { selectedTokens.remove(it) }
                                )
                            }
                            ExerciseType.FILL_IN_BLANK, ExerciseType.MULTIPLE_CHOICE -> {
                                OptionsExerciseView(
                                    options = currentExercise.options,
                                    selectedIndex = selectedOptionIndex,
                                    onSelect = { selectedOptionIndex = it }
                                )
                            }
                            ExerciseType.BUG_HUNT -> {
                                BugHuntExerciseView(
                                    lines = currentExercise.bugLines,
                                    selectedIndex = selectedBugLineIndex,
                                    onSelectLine = { selectedBugLineIndex = it }
                                )
                            }
                            ExerciseType.MATCH_PAIRS -> {
                                MatchPairsExerciseView(
                                    leftItems = currentExercise.pairLeft,
                                    rightItems = currentExercise.pairRight,
                                    selectedLeftIndex = selectedLeftIndex,
                                    matchedPairs = matchedPairs,
                                    onSelectLeft = { selectedLeftIndex = it },
                                    onSelectRight = { rightIdx ->
                                        if (selectedLeftIndex != -1) {
                                            if (selectedLeftIndex == rightIdx) {
                                                matchedPairs.add(selectedLeftIndex to rightIdx)
                                                selectedLeftIndex = -1
                                            } else {
                                                selectedLeftIndex = -1
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            } else {
                // Portrait Mobile Layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = currentExercise.prompt,
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp,
                        color = DuoTextDark,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentExercise.codeContext != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CodeBg)
                                .padding(14.dp)
                        ) {
                            Text(
                                text = currentExercise.codeContext,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                color = CodeFg,
                                lineHeight = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    when (currentExercise.type) {
                        ExerciseType.WORD_BANK -> {
                            WordBankExerciseView(
                                exercise = currentExercise,
                                selectedTokens = selectedTokens,
                                onAddToken = { selectedTokens.add(it) },
                                onRemoveToken = { selectedTokens.remove(it) }
                            )
                        }
                        ExerciseType.FILL_IN_BLANK, ExerciseType.MULTIPLE_CHOICE -> {
                            OptionsExerciseView(
                                options = currentExercise.options,
                                selectedIndex = selectedOptionIndex,
                                onSelect = { selectedOptionIndex = it }
                            )
                        }
                        ExerciseType.BUG_HUNT -> {
                            BugHuntExerciseView(
                                lines = currentExercise.bugLines,
                                selectedIndex = selectedBugLineIndex,
                                onSelectLine = { selectedBugLineIndex = it }
                            )
                        }
                        ExerciseType.MATCH_PAIRS -> {
                            MatchPairsExerciseView(
                                leftItems = currentExercise.pairLeft,
                                rightItems = currentExercise.pairRight,
                                selectedLeftIndex = selectedLeftIndex,
                                matchedPairs = matchedPairs,
                                onSelectLeft = { selectedLeftIndex = it },
                                onSelectRight = { rightIdx ->
                                    if (selectedLeftIndex != -1) {
                                        if (selectedLeftIndex == rightIdx) {
                                            matchedPairs.add(selectedLeftIndex to rightIdx)
                                            selectedLeftIndex = -1
                                        } else {
                                            selectedLeftIndex = -1
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Bottom Duolingo Response Sheet & Action Button
        BottomResultSheet(
            status = answerStatus,
            explanation = currentExercise.explanation,
            isCheckEnabled = isCheckEnabled,
            onCheck = {
                val isCorrect = when (currentExercise.type) {
                    ExerciseType.WORD_BANK -> {
                        selectedTokens.toList() == currentExercise.correctTokenSequence
                    }
                    ExerciseType.FILL_IN_BLANK, ExerciseType.MULTIPLE_CHOICE -> {
                        selectedOptionIndex == currentExercise.correctOptionIndex
                    }
                    ExerciseType.BUG_HUNT -> {
                        selectedBugLineIndex == currentExercise.bugLineIndex
                    }
                    ExerciseType.MATCH_PAIRS -> {
                        matchedPairs.size == currentExercise.pairLeft.size
                    }
                }

                if (isCorrect) {
                    answerStatus = AnswerStatus.CORRECT
                } else {
                    answerStatus = AnswerStatus.INCORRECT
                    // Push missed exercise to end of queue so user masters it without losing hearts!
                    exerciseQueue.add(currentExercise)
                }
            },
            onContinue = {
                if (answerStatus == AnswerStatus.CORRECT) {
                    completedExercisesCount++
                }
                if (currentQueueIndex + 1 < exerciseQueue.size) {
                    currentQueueIndex++
                } else {
                    isLessonCompleted = true
                }
            }
        )
    }
}

// -------------------------------------------------------------
// WORD BANK COMPOSABLE
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordBankExerciseView(
    exercise: Exercise,
    selectedTokens: List<String>,
    onAddToken: (String) -> Unit,
    onRemoveToken: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Answer Builder Tray
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, DuoCardBorder, RoundedCornerShape(12.dp))
                .background(Color(0xFFF9FAFB))
                .padding(12.dp)
        ) {
            if (selectedTokens.isEmpty()) {
                Text(
                    text = "Aşağıdaki kod bloklarına dokunarak buraya ekle...",
                    color = Color(0xFFAFAFAF),
                    fontSize = 13.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedTokens.forEach { token ->
                        TokenChip(
                            text = token,
                            onClick = { onRemoveToken(token) },
                            isInAnswerTray = true
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Scrambled Tokens
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            exercise.scrambledTokens.forEach { token ->
                val isUsed = selectedTokens.contains(token)
                TokenChip(
                    text = token,
                    onClick = {
                        if (!isUsed) onAddToken(token)
                    },
                    isUsed = isUsed,
                    isInAnswerTray = false
                )
            }
        }
    }
}

@Composable
private fun TokenChip(
    text: String,
    onClick: () -> Unit,
    isInAnswerTray: Boolean = false,
    isUsed: Boolean = false
) {
    val bgColor = when {
        isUsed -> Color(0xFFE5E7EB)
        isInAnswerTray -> Color(0xFFE8FCD0)
        else -> Color.White
    }
    val borderColor = when {
        isUsed -> Color(0xFFD1D5DB)
        isInAnswerTray -> DuoGreen
        else -> DuoCardBorder
    }
    val textColor = when {
        isUsed -> Color(0xFF9CA3AF)
        isInAnswerTray -> DuoGreenDark
        else -> DuoTextDark
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(enabled = !isUsed, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = textColor
        )
    }
}

// -------------------------------------------------------------
// OPTIONS / MULTIPLE CHOICE
// -------------------------------------------------------------
@Composable
private fun OptionsExerciseView(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            val borderColor = if (isSelected) DuoBlue else DuoCardBorder
            val bgColor = if (isSelected) DuoBlue.copy(alpha = 0.08f) else Color.White

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor)
                    .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                    .clickable { onSelect(index) }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) DuoBlue else Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${('A' + index)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isSelected) Color.White else DuoTextDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = option,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = if (option.contains("()") || option.contains(".")) FontFamily.Monospace else FontFamily.Default,
                    color = DuoTextDark
                )
            }
        }
    }
}

// -------------------------------------------------------------
// BUG HUNT COMPOSABLE
// -------------------------------------------------------------
@Composable
private fun BugHuntExerciseView(
    lines: List<String>,
    selectedIndex: Int,
    onSelectLine: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CodeBg)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        lines.forEachIndexed { index, line ->
            val isSelected = selectedIndex == index
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) DuoRed.copy(alpha = 0.3f) else Color.Transparent)
                    .clickable { onSelectLine(index) }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}",
                    color = Color(0xFF6B7280),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    modifier = Modifier.width(24.dp)
                )
                Text(
                    text = line,
                    color = if (isSelected) Color(0xFFFF8B8B) else CodeFg,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// MATCH PAIRS COMPOSABLE
// -------------------------------------------------------------
@Composable
private fun MatchPairsExerciseView(
    leftItems: List<String>,
    rightItems: List<String>,
    selectedLeftIndex: Int,
    matchedPairs: List<Pair<Int, Int>>,
    onSelectLeft: (Int) -> Unit,
    onSelectRight: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Left Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            leftItems.forEachIndexed { index, text ->
                val isMatched = matchedPairs.any { it.first == index }
                val isSelected = selectedLeftIndex == index
                MatchCard(
                    text = text,
                    isMatched = isMatched,
                    isSelected = isSelected,
                    onClick = { if (!isMatched) onSelectLeft(index) }
                )
            }
        }

        // Right Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rightItems.forEachIndexed { index, text ->
                val isMatched = matchedPairs.any { it.second == index }
                MatchCard(
                    text = text,
                    isMatched = isMatched,
                    isSelected = false,
                    onClick = { if (!isMatched) onSelectRight(index) }
                )
            }
        }
    }
}

@Composable
private fun MatchCard(
    text: String,
    isMatched: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isMatched -> DuoGreen
        isSelected -> DuoBlue
        else -> DuoCardBorder
    }
    val bgColor = when {
        isMatched -> DuoGreen.copy(alpha = 0.15f)
        isSelected -> DuoBlue.copy(alpha = 0.15f)
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = !isMatched, onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isMatched) DuoGreenDark else DuoTextDark,
            textAlign = TextAlign.Center
        )
    }
}

// -------------------------------------------------------------
// DUOLINGO BOTTOM RESULT POPUP & BUTTON
// -------------------------------------------------------------
@Composable
private fun BottomResultSheet(
    status: AnswerStatus,
    explanation: String,
    isCheckEnabled: Boolean,
    onCheck: () -> Unit,
    onContinue: () -> Unit
) {
    val sheetBg = when (status) {
        AnswerStatus.UNANSWERED -> Color.White
        AnswerStatus.CORRECT -> DuoGreenLight
        AnswerStatus.INCORRECT -> DuoRedLight
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(sheetBg)
            .border(
                width = 1.dp,
                color = when (status) {
                    AnswerStatus.UNANSWERED -> DuoCardBorder
                    AnswerStatus.CORRECT -> DuoGreen
                    AnswerStatus.INCORRECT -> DuoRed
                },
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        if (status == AnswerStatus.CORRECT) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = DuoGreen,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Harika!",
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp,
                        color = DuoGreenDark
                    )
                    Text(
                        text = "+15 XP kazandın!",
                        fontSize = 12.sp,
                        color = DuoGreenDark
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        } else if (status == AnswerStatus.INCORRECT) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ReportProblem,
                    contentDescription = null,
                    tint = DuoRed,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Neredeyse oldu! (Can Gitmedi ∞)",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = DuoRed
                    )
                    Text(
                        text = explanation,
                        fontSize = 12.sp,
                        color = DuoTextDark,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Action Button (KONTROL ET or DEVAM ET)
        if (status == AnswerStatus.UNANSWERED) {
            DuoTextButton(
                text = "KONTROL ET",
                onClick = onCheck,
                color = DuoButtonColor.GREEN,
                enabled = isCheckEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("check_answer_button")
            )
        } else {
            DuoTextButton(
                text = "DEVAM ET",
                onClick = onContinue,
                color = if (status == AnswerStatus.CORRECT) DuoButtonColor.GREEN else DuoButtonColor.RED,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("continue_lesson_button")
            )
        }
    }
}

// -------------------------------------------------------------
// VICTORY CELEBRATION VIEW
// -------------------------------------------------------------
@Composable
private fun LessonVictoryView(
    lesson: Lesson,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mascot Celebration Image
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(DuoYellow.copy(alpha = 0.2f))
                .border(3.dp, DuoYellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.mascot_byte_happy_1790508489012),
                contentDescription = "Tebrikler",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Ders Tamamlandı!",
            fontWeight = FontWeight.Black,
            fontSize = 26.sp,
            color = DuoGreenDark
        )

        Text(
            text = "${lesson.title} konusunu başarıyla kavradın.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Stats Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // XP Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DuoYellowLight)
                    .border(2.dp, DuoYellow, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "⚡ XP", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFB45309))
                    Text(text = "+${lesson.xpReward}", fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color(0xFFB45309))
                }
            }

            // Gems Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0F2FE))
                    .border(2.dp, Color(0xFF38BDF8), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💎 MÜCEVHER", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0369A1))
                    Text(text = "+${lesson.gemReward}", fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color(0xFF0369A1))
                }
            }

            // Streak Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DuoGreenLight)
                    .border(2.dp, DuoGreen, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔥 SERİ", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DuoGreenDark)
                    Text(text = "Aktif", fontWeight = FontWeight.Black, fontSize = 20.sp, color = DuoGreenDark)
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        DuoTextButton(
            text = "YOL HARİTASINA DÖN",
            onClick = onContinue,
            color = DuoButtonColor.GREEN,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
