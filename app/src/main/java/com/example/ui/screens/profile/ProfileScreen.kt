package com.example.ui.screens.profile

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Achievement
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.components.DuoTextButton
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoTextDark
import com.example.ui.theme.DuoYellow

@Composable
fun ProfileScreen(
    streak: Int,
    totalXp: Int,
    gems: Int,
    completedLessonsCount: Int,
    achievements: List<Achievement>,
    onOpenDebApkDialog: () -> Unit,
    onOpenHeartsDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(2.dp, DuoCardBorder, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(DuoGreen.copy(alpha = 0.2f))
                            .border(3.dp, DuoGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.mascot_byte_happy_1790508489012),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Oyun Kodlayıcısı",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = DuoTextDark
                        )
                        Text(
                            text = "Duolingo Kod Şampiyonu",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Infinite Hearts Badge in Profile
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DuoGreen.copy(alpha = 0.1f))
                                .clickable { onOpenHeartsDialog() }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = DuoRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sınırsız Can Modu ∞",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = DuoGreenDark
                            )
                        }
                    }
                }
            }
        }

        // Stats Overview Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak Card
                StatCard(
                    icon = "🔥",
                    title = "Günlük Seri",
                    value = "$streak Gün",
                    valueColor = DuoOrange,
                    modifier = Modifier.weight(1f)
                )

                // Total XP Card
                StatCard(
                    icon = "⚡",
                    title = "Toplam XP",
                    value = "$totalXp",
                    valueColor = Color(0xFFB45309),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Gems Card
                StatCard(
                    icon = "💎",
                    title = "Mücevher",
                    value = "$gems",
                    valueColor = DuoBlue,
                    modifier = Modifier.weight(1f)
                )

                // Completed Lessons Card
                StatCard(
                    icon = "🏆",
                    title = "Biten Ders",
                    value = "$completedLessonsCount",
                    valueColor = DuoGreenDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // APK & Debian (.deb) Special Card (Directly addresses user's query!)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(2.dp, DuoBlue.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DuoBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = null,
                                tint = DuoBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = ".deb ve .apk Kurulum Rehberi",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DuoTextDark
                            )
                            Text(
                                text = "Arşiv İmzası Hatasız Paketleme Bilgisi",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "APK dosyası V1/V2 geçerli imza anahtarıyla yapılandırılmıştır. Debian (.deb) paketi için terminal dönüştürme scriptini görüntüle.",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DuoTextButton(
                        text = "REHBER VE SCRIPTI GÖRÜNTÜLE",
                        onClick = onOpenDebApkDialog,
                        color = DuoButtonColor.BLUE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_deb_apk_guide_button")
                    )
                }
            }
        }

        // Achievements Section
        item {
            Text(
                text = "BAŞARILAR & ROZETLER",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                letterSpacing = 0.5.sp
            )
        }

        items(achievements) { ach ->
            val borderColor = if (ach.isUnlocked) DuoYellow else DuoCardBorder
            val bgColor = if (ach.isUnlocked) DuoYellow.copy(alpha = 0.05f) else Color.White

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (ach.isUnlocked) DuoYellow.copy(alpha = 0.2f) else Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = ach.iconEmoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = ach.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DuoTextDark
                            )
                            if (ach.isUnlocked) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AÇILDI ✓",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFD97706)
                                )
                            }
                        }
                        Text(
                            text = ach.description,
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        val progress = (ach.progress.toFloat() / ach.maxProgress.toFloat()).coerceIn(0f, 1f)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFE5E5E5))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (ach.isUnlocked) DuoYellow else DuoBlue)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${ach.progress}/${ach.maxProgress}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: String,
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(2.dp, DuoCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = valueColor
            )
        }
    }
}
