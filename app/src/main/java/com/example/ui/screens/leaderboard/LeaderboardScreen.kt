package com.example.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.example.data.model.LeagueTier
import com.example.data.model.LeagueUser
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoTextDark
import com.example.ui.theme.DuoYellow

@Composable
fun LeaderboardScreen(
    currentTier: LeagueTier,
    users: List<LeagueUser>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // League Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(DuoYellow)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${currentTier.emoji} ${currentTier.title.uppercase()}",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color(0xFF78350F)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "İlk 3 kişi sonraki lige yükselir!",
                            fontSize = 13.sp,
                            color = Color(0xFF78350F).copy(alpha = 0.9f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "3 Gün Kaldı",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF78350F)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Leaderboard List
        itemsIndexed(users) { index, user ->
            val isPromotionZone = index < 3
            val isCurrentUser = user.isCurrentUser

            // Card background & border
            val cardBg = if (isCurrentUser) DuoGreen.copy(alpha = 0.12f) else Color.White
            val cardBorder = if (isCurrentUser) DuoGreen else DuoCardBorder

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .border(2.dp, cardBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rank indicator
                    Box(
                        modifier = Modifier.width(32.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        when (user.rank) {
                            1 -> Text(text = "🥇", fontSize = 20.sp)
                            2 -> Text(text = "🥈", fontSize = 20.sp)
                            3 -> Text(text = "🥉", fontSize = 20.sp)
                            else -> Text(
                                text = "${user.rank}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = user.avatarEmoji, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Username & badge
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.name,
                                fontWeight = if (isCurrentUser) FontWeight.Black else FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isCurrentUser) DuoGreenDark else DuoTextDark
                            )
                            if (user.badge != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = user.badge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD97706),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFEF3C7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // User XP
                    Text(
                        text = "${user.xp} XP",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                }

                // Green divider for promotion zone
                if (index == 2) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .background(DuoGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "YÜKSELME ÇİZGİSİ",
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = DuoGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .background(DuoGreen)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}
