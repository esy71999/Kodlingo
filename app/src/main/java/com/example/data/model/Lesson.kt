package com.example.data.model

enum class ExerciseType {
    WORD_BANK,       // Kod parçalarını doğru sırayla dizme (Duolingo style)
    FILL_IN_BLANK,   // Boşluğa gelecek anahtar kelimeyi seçme
    MULTIPLE_CHOICE, // Soru & çıktı tahmini
    BUG_HUNT,        // Hatalı satırı tespit etme
    MATCH_PAIRS      // Terim ve anlam eşleştirme kartları
}

data class Exercise(
    val id: String,
    val type: ExerciseType,
    val prompt: String,             // Soru başlığı / talimatı
    val codeContext: String? = null,// Ekranda gösterilecek kod bloku (opsiyonel)
    // WORD_BANK için:
    val scrambledTokens: List<String> = emptyList(), // Karışık butonlar
    val correctTokenSequence: List<String> = emptyList(), // Doğru sıra
    // FILL_IN_BLANK veya MULTIPLE_CHOICE için:
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = -1,
    // BUG_HUNT için:
    val bugLines: List<String> = emptyList(),
    val bugLineIndex: Int = -1,
    // MATCH_PAIRS için:
    val pairLeft: List<String> = emptyList(),
    val pairRight: List<String> = emptyList(),
    val explanation: String        // Yanlış yapıldığında gösterilen açıklama (Can gitmez, öğretir!)
)

data class Lesson(
    val id: String,
    val unitNumber: Int,
    val title: String,
    val subtitle: String,
    val xpReward: Int = 20,
    val gemReward: Int = 10,
    val iconEmoji: String = "🎮",
    val exercises: List<Exercise>
)

data class UnitData(
    val unitNumber: Int,
    val title: String,
    val description: String,
    val themeColorHex: String,
    val lessons: List<Lesson>
)
