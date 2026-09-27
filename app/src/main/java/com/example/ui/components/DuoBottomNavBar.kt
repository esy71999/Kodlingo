package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.viewmodel.NavigationTab

data class BottomNavItem(
    val tab: NavigationTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun DuoBottomNavBar(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(NavigationTab.LEARN, "Öğren", Icons.Filled.School, Icons.Outlined.School, "tab_learn"),
        BottomNavItem(NavigationTab.ARENA, "Pratik", Icons.Filled.FlashOn, Icons.Outlined.FlashOn, "tab_arena"),
        BottomNavItem(NavigationTab.LEADERBOARD, "Ligler", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_leaderboard"),
        BottomNavItem(NavigationTab.SANDBOX, "Kod Alanı", Icons.Filled.Code, Icons.Outlined.Code, "tab_sandbox"),
        BottomNavItem(NavigationTab.PROFILE, "Profil", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(
                width = 1.dp,
                color = DuoCardBorder,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.tab == selectedTab
                val tint = if (isSelected) DuoGreen else Color(0xFF9E9E9E)

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag(item.testTag),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = tint
                    )
                }
            }
        }
    }
}

@Composable
fun DuoNavRail(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(NavigationTab.LEARN, "Öğren", Icons.Filled.School, Icons.Outlined.School, "tab_learn"),
        BottomNavItem(NavigationTab.ARENA, "Pratik", Icons.Filled.FlashOn, Icons.Outlined.FlashOn, "tab_arena"),
        BottomNavItem(NavigationTab.LEADERBOARD, "Ligler", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_leaderboard"),
        BottomNavItem(NavigationTab.SANDBOX, "Kod Alanı", Icons.Filled.Code, Icons.Outlined.Code, "tab_sandbox"),
        BottomNavItem(NavigationTab.PROFILE, "Profil", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
    )

    Box(
        modifier = modifier
            .background(Color.White)
            .border(width = 1.dp, color = DuoCardBorder)
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items.forEach { item ->
                val isSelected = item.tab == selectedTab
                val tint = if (isSelected) DuoGreen else Color(0xFF9E9E9E)
                val bgColor = if (isSelected) DuoGreen.copy(alpha = 0.1f) else Color.Transparent

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("rail_${item.testTag}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = tint,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = tint
                    )
                }
            }
        }
    }
}
