package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CodingLanguage
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoTextDark

@Composable
fun DuoTopBar(
    currentLanguage: CodingLanguage,
    streak: Int,
    gems: Int,
    xp: Int,
    onLanguageClick: () -> Unit,
    onHeartsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .border(
                width = 1.dp,
                color = DuoCardBorder,
                shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language selector button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, DuoCardBorder, RoundedCornerShape(12.dp))
                    .clickable(onClick = onLanguageClick)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("language_selector_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentLanguage.iconEmoji,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentLanguage.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DuoTextDark
                )
            }

            // Streak Flame
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Günlük Seri",
                    tint = DuoOrange,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$streak",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = DuoOrange
                )
            }

            // Gems / Crystals
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💎",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$gems",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color(0xFF1CB0F6)
                )
            }

            // Infinite Hearts Badge (User requested: Can sistemi olmasın)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.5.dp, DuoGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .background(DuoGreen.copy(alpha = 0.1f))
                    .clickable(onClick = onHeartsClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("infinite_hearts_badge"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Sınırsız Can",
                    tint = DuoRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.AllInclusive,
                    contentDescription = "Sonsuz",
                    tint = DuoGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
