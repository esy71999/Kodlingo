package com.example.data.model

data class CodingLanguage(
    val id: String,
    val name: String,
    val description: String,
    val category: String, // "Oyun Motoru", "Genel & Script", "Sistem & Hızlı"
    val iconEmoji: String,
    val accentColorHex: String,
    val totalLessons: Int,
    val tags: List<String>
)

object SupportedLanguages {
    val languages = listOf(
        CodingLanguage(
            id = "gdscript",
            name = "GDScript",
            description = "Godot Engine 4 ile 2D & 3D bağımsız oyun geliştirme dili.",
            category = "Oyun Motoru",
            iconEmoji = "🔵",
            accentColorHex = "#478CBF",
            totalLessons = 16,
            tags = listOf("Godot 4", "2D/3D", "Fizik", "Sinyaller")
        ),
        CodingLanguage(
            id = "lua",
            name = "Lua",
            description = "Roblox Studio, LÖVE2D ve oyun eklentileri geliştirme dili.",
            category = "Oyun & Script",
            iconEmoji = "🌙",
            accentColorHex = "#000080",
            totalLessons = 16,
            tags = listOf("Roblox", "Love2D", "Hızlı", "Modlama")
        ),
        CodingLanguage(
            id = "python",
            name = "Python",
            description = "Pygame ile oyun mekanikleri, veri yapıları ve yapay zeka.",
            category = "Genel & Oyun",
            iconEmoji = "🐍",
            accentColorHex = "#3776AB",
            totalLessons = 16,
            tags = listOf("Pygame", "Temiz Kod", "Matematik", "Yapay Zeka")
        ),
        CodingLanguage(
            id = "csharp",
            name = "C# (CSharp)",
            description = "Unity Engine ile profesyonel mobil, PC ve konsol oyunları.",
            category = "Oyun Motoru",
            iconEmoji = "🟩",
            accentColorHex = "#239120",
            totalLessons = 14,
            tags = listOf("Unity", "MonoBehaviour", "Fizik", "Nesne Yönelimli")
        ),
        CodingLanguage(
            id = "cpp",
            name = "C++",
            description = "Unreal Engine ve yüksek performanslı AAA oyun motoru geliştirme.",
            category = "Sistem & Oyun",
            iconEmoji = "⚡",
            accentColorHex = "#00599C",
            totalLessons = 14,
            tags = listOf("Unreal Engine", "Hız", "Bellek", "Game Loop")
        ),
        CodingLanguage(
            id = "javascript",
            name = "JavaScript",
            description = "Phaser.js ve HTML5 Canvas ile web tabanlı tarayıcı oyunları.",
            category = "Web & Oyun",
            iconEmoji = "🟨",
            accentColorHex = "#F7DF1E",
            totalLessons = 12,
            tags = listOf("Phaser", "HTML5", "Canvas", "Web Oyunu")
        ),
        CodingLanguage(
            id = "rust",
            name = "Rust",
            description = "Bevy Engine ile modern ECS mimarisi ve güvenli oyun kodlama.",
            category = "Modern Sistem",
            iconEmoji = "🦀",
            accentColorHex = "#DEA584",
            totalLessons = 12,
            tags = listOf("Bevy", "ECS", "Bellek Güvenliği", "Yeni Nesil")
        )
    )

    fun getById(id: String): CodingLanguage {
        return languages.find { it.id == id } ?: languages[0]
    }
}
