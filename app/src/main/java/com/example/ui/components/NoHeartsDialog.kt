package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoTextDark

@Composable
fun NoHeartsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DuoTextButton(
                text = "HARİKA, ANLADIM!",
                onClick = onDismiss,
                color = DuoButtonColor.GREEN,
                modifier = Modifier.fillMaxWidth()
            )
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(DuoGreen.copy(alpha = 0.15f))
                        .border(2.dp, DuoGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = DuoRed,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.AllInclusive,
                            contentDescription = null,
                            tint = DuoGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Sınırsız Can Modu Aktif!",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = DuoTextDark,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF7F7F7))
                    .padding(14.dp)
            ) {
                Text(
                    text = "CodeLingo'da can veya hak tükenmesi yoktur.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DuoGreen,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = "• Hata yaptığında canın gitmez, ders kilitlenmez.\n• Yanlış yaptığın soru konsepti dersin sonuna eklenir ve doğru öğrenene kadar sınırsız tekrar edebilirsin.\n• Oyun kodlama dillerini (Godot, Roblox Lua, Python, Unity C#) stressiz pratik yap!",
                    fontSize = 13.sp,
                    color = Color(0xFF4B4B4B),
                    lineHeight = 18.sp
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}
