package com.example.ui.screens.sandbox

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SnippetEntity
import com.example.data.model.CodingLanguage
import com.example.data.model.SupportedLanguages
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.components.DuoTextButton
import com.example.ui.theme.CodeBg
import com.example.ui.theme.CodeFg
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoTextDark

@Composable
fun CodeSandboxScreen(
    currentLanguage: CodingLanguage,
    savedSnippets: List<SnippetEntity>,
    onSaveSnippet: (title: String, code: String) -> Unit,
    onDeleteSnippet: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    // Pre-loaded sample game scripts
    val sampleScripts = remember(selectedLanguage.id) {
        when (selectedLanguage.id) {
            "gdscript" -> """
# Godot 4: 2D Karakter Hareketi
extends CharacterBody2D

const HIZ = 300.0
const ZIPLAMA = -400.0
var yercekimi = ProjectSettings.get_setting("physics/2d/default_gravity")

func _ready():
    print("🎬 Godot Karakteri Sahneye Girdi!")

func _physics_process(delta):
    if not is_on_floor():
        velocity.y += yercekimi * delta
    
    var yon = Input.get_axis("ui_left", "ui_right")
    velocity.x = yon * HIZ
    move_and_slide()
    print("Pozisyon: ", position)
            """.trimIndent()

            "lua" -> """
-- Roblox Studio: Dokununca Hasar Veren Lav Bloğu
local part = script.Parent
local damage = 25

part.Touched:Connect(function(hit)
    local humanoid = hit.Parent:FindFirstChild("Humanoid")
    if humanoid then
        humanoid:TakeDamage(damage)
        print("🔥 Oyuncuya " .. damage .. " hasar verildi!")
    end
end)
print("Lav bloğu aktif!")
            """.trimIndent()

            "python" -> """
# Pygame: Oyun Döngüsü ve Skor
import pygame
pygame.init()

skor = 100
can = 3
oyun_bitti = False

print("🕹️ Pygame Motoru Başlatıldı!")
print(f"Başlangıç Skoru: {skor} | Kalan Can: {can}")

for frame in range(1, 4):
    skor += 50
    print(f"Kare #{frame}: Altın Toplandı! Yeni Skor: {skor}")
            """.trimIndent()

            "csharp" -> """
// Unity Engine: Karakter Zıplama
using UnityEngine;

public class PlayerJump : MonoBehaviour {
    public float jumpForce = 8f;
    private Rigidbody2D rb;

    void Start() {
        rb = GetComponent<Rigidbody2D>();
        Debug.Log("🎮 Unity Oyuncu Başlatıldı!");
    }

    void Update() {
        if (Input.GetKeyDown(KeyCode.Space)) {
            rb.linearVelocity = Vector2.up * jumpForce;
            Debug.Log("Zıplama Tetiklendi!");
        }
    }
}
            """.trimIndent()

            else -> """
// ${selectedLanguage.name} Oyun Kodu
print("Kod alanı çalışıyor!");
            """.trimIndent()
        }
    }

    var codeText by remember(sampleScripts) { mutableStateOf(sampleScripts) }
    var consoleOutput by remember { mutableStateOf("Konsol çıktısı burada görüntülenecek...") }
    var isRunning by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Title & Description
        item {
            Column {
                Text(
                    text = "İnteraktif Kod Alanı & Oyun Simülatörü",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = DuoTextDark
                )
                Text(
                    text = "Godot, Roblox Lua, Python ve C# kodlarını yaz, test et ve simüle et.",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Language Selector Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SupportedLanguages.languages) { lang ->
                    val isSelected = lang.id == selectedLanguage.id
                    val bgColor = if (isSelected) DuoGreen else Color.White
                    val textColor = if (isSelected) Color.White else DuoTextDark
                    val borderColor = if (isSelected) DuoGreenDark else DuoCardBorder

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                            .clickable { selectedLanguage = lang }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${lang.iconEmoji} ${lang.name}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = textColor
                        )
                    }
                }
            }
        }

        // Code Editor and Console (Adaptive Side-by-Side in Landscape/Desktop)
        item {
            androidx.compose.foundation.layout.BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val isWide = maxWidth >= 600.dp
                if (isWide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Left Column: Editor & Run Button
                        Column(modifier = Modifier.weight(1.1f)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(CodeBg)
                                    .border(2.dp, Color(0xFF2E3440), RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Code,
                                                contentDescription = null,
                                                tint = Color(0xFF88C0D0),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "${selectedLanguage.name} Editörü",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFD8DEE9)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                onSaveSnippet("${selectedLanguage.name} Kodu", codeText)
                                                Toast.makeText(context, "Kod başarıyla kaydedildi!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.BookmarkAdd,
                                                contentDescription = "Kaydet",
                                                tint = Color(0xFF88C0D0),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = codeText,
                                        onValueChange = { codeText = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(260.dp)
                                            .testTag("code_editor_field"),
                                        textStyle = TextStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            color = CodeFg,
                                            lineHeight = 17.sp
                                        ),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            cursorColor = DuoGreen
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            DuoTextButton(
                                text = "▶ KODU ÇALIŞTIR",
                                onClick = {
                                    isRunning = true
                                    consoleOutput = getSimulatedOutput(selectedLanguage.id)
                                    isRunning = false
                                },
                                color = DuoButtonColor.GREEN,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("run_code_button")
                            )
                        }

                        // Right Column: Terminal Console
                        Column(modifier = Modifier.weight(0.9f)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(320.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF0F141C))
                                    .border(2.dp, Color(0xFF1E2638), RoundedCornerShape(14.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Terminal,
                                            contentDescription = null,
                                            tint = Color(0xFFA3BE8C),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Konsol Çıktısı (Debug Output)",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFA3BE8C)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(
                                            text = consoleOutput,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = Color(0xFFECEFF4),
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Mobile Portrait Layout
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(CodeBg)
                                .border(2.dp, Color(0xFF2E3440), RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Code,
                                            contentDescription = null,
                                            tint = Color(0xFF88C0D0),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${selectedLanguage.name} Editörü",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD8DEE9)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            onSaveSnippet("${selectedLanguage.name} Kodu", codeText)
                                            Toast.makeText(context, "Kod başarıyla kaydedildi!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.BookmarkAdd,
                                            contentDescription = "Kaydet",
                                            tint = Color(0xFF88C0D0),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = codeText,
                                    onValueChange = { codeText = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .testTag("code_editor_field"),
                                    textStyle = TextStyle(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = CodeFg,
                                        lineHeight = 17.sp
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        cursorColor = DuoGreen
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        DuoTextButton(
                            text = "▶ KODU ÇALIŞTIR",
                            onClick = {
                                isRunning = true
                                consoleOutput = getSimulatedOutput(selectedLanguage.id)
                                isRunning = false
                            },
                            color = DuoButtonColor.GREEN,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("run_code_button")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0F141C))
                                .border(2.dp, Color(0xFF1E2638), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Terminal,
                                        contentDescription = null,
                                        tint = Color(0xFFA3BE8C),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Konsol Çıktısı (Debug Output)",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFA3BE8C)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = consoleOutput,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = Color(0xFFECEFF4),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Saved Snippets List
        if (savedSnippets.isNotEmpty()) {
            item {
                Text(
                    text = "KAYDEDİLEN KOD PARÇALARI",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    letterSpacing = 0.5.sp
                )
            }

            items(savedSnippets) { snippet ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.5.dp, DuoCardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    codeText = snippet.code
                                    Toast.makeText(context, "Kod editöre yüklendi!", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Text(
                                text = snippet.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DuoTextDark
                            )
                            Text(
                                text = snippet.code.lines().firstOrNull() ?: "",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color(0xFF777777),
                                maxLines = 1
                            )
                        }

                        IconButton(
                            onClick = { onDeleteSnippet(snippet.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Sil",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getSimulatedOutput(languageId: String): String {
    return when (languageId) {
        "gdscript" -> """
[Godot 4.3 Engine]: Sahne yükleniyor...
🎬 Godot Karakteri Sahneye Girdi!
[PhysicsServer2D]: Karakter fiziği başlatıldı (HIZ: 300.0, FPS: 60)
Pozisyon: (0.0, 0.0)
Pozisyon: (15.0, 0.0)
Pozisyon: (30.0, 0.0)
✅ Godot scripti sıfır hata ile çalıştı!
        """.trimIndent()

        "lua" -> """
[Roblox Studio]: Script başlatıldı.
Lav bloğu aktif!
[Event]: Touched sinyali dinleniyor...
🔥 Oyuncuya 25 hasar verildi!
[Output]: Karakter canı: 75/100
✅ Lua scripti başarıyla derlendi!
        """.trimIndent()

        "python" -> """
[Pygame 2.5]: Ekran oluşturuldu.
🕹️ Pygame Motoru Başlatıldı!
Başlangıç Skoru: 100 | Kalan Can: 3
Kare #1: Altın Toplandı! Yeni Skor: 150
Kare #2: Altın Toplandı! Yeni Skor: 200
Kare #3: Altın Toplandı! Yeni Skor: 250
✅ Pygame döngüsü hatasız tamamlandı!
        """.trimIndent()

        "csharp" -> """
[Unity Engine 2024.1]: Komut dosyası derlendi.
🎮 Unity Oyuncu Başlatıldı!
[Rigidbody2D]: Fizik döngüsü aktif.
Zıplama Tetiklendi! Velocity: (0.0, 8.0)
✅ C# derleme başarılı!
        """.trimIndent()

        else -> "Script çıktı: Kod başarıyla çalıştırıldı."
    }
}
