package com.example.data.model

data class LeagueUser(
    val rank: Int,
    val name: String,
    val xp: Int,
    val avatarEmoji: String,
    val isCurrentUser: Boolean = false,
    val badge: String? = null
)

enum class LeagueTier(
    val title: String,
    val emoji: String,
    val colorHex: String,
    val minXp: Int
) {
    BRONZE("Bronz Ligi", "🥉", "#CD7F32", 0),
    SILVER("Gümüş Ligi", "🥈", "#C0C0C0", 150),
    GOLD("Altın Ligi", "🥇", "#FFD700", 400),
    DIAMOND("Elmas Ligi", "💎", "#00BFFF", 800),
    OBSIDIAN("Obsidyen Şampiyonlar", "👑", "#9B59B6", 1500)
}
