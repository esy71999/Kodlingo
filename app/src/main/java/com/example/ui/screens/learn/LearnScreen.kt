package com.example.ui.screens.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CodingLanguage
import com.example.data.model.Lesson
import com.example.data.model.UnitData
import com.example.ui.components.DuoPathNode
import com.example.ui.components.MascotBubble
import com.example.ui.components.NodeState
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoTextDark
import com.example.ui.theme.DuoYellow

@Composable
fun LearnScreen(
    currentLanguage: CodingLanguage,
    units: List<UnitData>,
    completedLessonIds: Set<String>,
    onSelectLesson: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Mascot Greeting
        item {
            MascotBubble(
                message = "${currentLanguage.name} ile oyun kodlama yolculuğuna devam et!",
                subMessage = "Can sistemi yok, dilediğin kadar hata yapıp öğrenebilirsin. ∞"
            )
        }

        // Units and their Path Nodes
        units.forEachIndexed { unitIndex, unitData ->
            // Unit Header Banner (Duolingo style)
            item(key = "unit_header_${unitData.unitNumber}") {
                UnitHeaderCard(
                    unit = unitData,
                    unitColor = parseColor(unitData.themeColorHex)
                )
            }

            // Path Nodes for this unit
            val unitLessons = unitData.lessons
            itemsIndexed(unitLessons) { lessonIndex, lesson ->
                val isCompleted = completedLessonIds.contains(lesson.id)
                // The first incomplete lesson in the whole list is CURRENT, subsequent ones are LOCKED
                val isCurrent = !isCompleted && isFirstIncomplete(units, completedLessonIds, lesson.id)
                val nodeState = when {
                    isCompleted -> NodeState.COMPLETED
                    isCurrent -> NodeState.CURRENT
                    else -> NodeState.LOCKED
                }

                // Zigzag offset pattern: 0 -> 0f, 1 -> 0.35f, 2 -> 0f, 3 -> -0.35f...
                val offsetFraction = when (lessonIndex % 4) {
                    0 -> 0f
                    1 -> 0.32f
                    2 -> 0f
                    3 -> -0.32f
                    else -> 0f
                }

                DuoPathNode(
                    lessonTitle = lesson.title,
                    lessonSubtitle = lesson.subtitle,
                    iconEmoji = lesson.iconEmoji,
                    state = nodeState,
                    horizontalOffsetFraction = offsetFraction,
                    onClick = {
                        onSelectLesson(lesson)
                    }
                )
            }

            // Chest / Checkpoint at the end of each unit
            item(key = "unit_chest_${unitData.unitNumber}") {
                UnitChestCard(unitNumber = unitData.unitNumber)
            }
        }
    }
}

private fun isFirstIncomplete(units: List<UnitData>, completedSet: Set<String>, targetLessonId: String): Boolean {
    for (unit in units) {
        for (lesson in unit.lessons) {
            if (!completedSet.contains(lesson.id)) {
                return lesson.id == targetLessonId
            }
        }
    }
    return false
}

@Composable
fun UnitHeaderCard(
    unit: UnitData,
    unitColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(unitColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = unit.title.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = unit.description,
                    fontSize = 13.sp,
                    color = Color.White,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .border(1.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = "Rehber Kitap",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun UnitChestCard(
    unitNumber: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(2.dp, DuoCardBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DuoYellow.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Hazine Sandığı",
                        tint = DuoYellow,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Ünite $unitNumber Ödül Sandığı",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DuoTextDark
                    )
                    Text(
                        text = "Dersleri bitirince +50 Mücevher!",
                        fontSize = 11.sp,
                        color = Color(0xFF777777)
                    )
                }
            }
        }
    }
}

private fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        DuoGreen
    }
}
