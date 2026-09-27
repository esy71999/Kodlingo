package com.example.ui.screens.arena

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyQuest
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.components.DuoTextButton
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoTextDark
import com.example.ui.theme.DuoYellow

@Composable
fun ArenaScreen(
    dailyQuests: List<DailyQuest>,
    onStartSpeedDrill: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card: Bug Hunt Arena
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(DuoBlue)
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "HIZLI HATA AVI (BUG HUNT)",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                            Text(
                                text = "60 saniyede oyun kodlarındaki hataları bul!",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DuoTextButton(
                        text = "MEYDAN OKUMAYA BAŞLA (+40 XP)",
                        onClick = onStartSpeedDrill,
                        color = DuoButtonColor.ORANGE,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Daily Quests Section
        item {
            Text(
                text = "GÜNLÜK GÖREVLER",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                letterSpacing = 0.5.sp
            )
        }

        items(dailyQuests) { quest ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(2.dp, DuoCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = quest.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = DuoTextDark
                            )
                            Text(
                                text = quest.description,
                                fontSize = 12.sp,
                                color = Color(0xFF777777),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        // XP Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(DuoYellow.copy(alpha = 0.15f))
                                .border(1.dp, DuoYellow, RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+${quest.xpReward} XP",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar
                    val progress = (quest.current.toFloat() / quest.target.toFloat()).coerceIn(0f, 1f)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(10.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE5E5E5))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (quest.isCompleted) DuoGreen else DuoOrange)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${quest.current}/${quest.target}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        // Game Dev Skills Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(2.dp, DuoCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DuoGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = DuoGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Oyun Geliştirici Becerileri",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DuoTextDark
                        )
                        Text(
                            text = "Godot 4, Roblox Lua ve Pygame'de ustalaş.",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }
        }
    }
}
