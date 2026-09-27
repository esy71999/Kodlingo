package com.example.ui.screens.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CodingLanguage
import com.example.data.model.SupportedLanguages
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoTextDark

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LanguagePickerModal(
    currentLanguageId: String,
    onLanguageSelected: (CodingLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Oyun ve Kodlama Dili Seç",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = DuoTextDark
            )
            Text(
                text = "İstediğin zaman diller arasında geçiş yapabilirsin.",
                fontSize = 13.sp,
                color = Color(0xFF777777),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(SupportedLanguages.languages) { lang ->
                    val isSelected = lang.id == currentLanguageId
                    val borderColor = if (isSelected) DuoGreen else DuoCardBorder
                    val bgColor = if (isSelected) DuoGreen.copy(alpha = 0.08f) else Color.White

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                            .clickable {
                                onLanguageSelected(lang)
                                onDismiss()
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF2F4F7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = lang.iconEmoji, fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = lang.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DuoTextDark
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = lang.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF6B7280),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFE5E7EB))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lang.description,
                                fontSize = 12.sp,
                                color = Color(0xFF666666),
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                lang.tags.forEach { tag ->
                                    Text(
                                        text = "#$tag",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF478CBF)
                                    )
                                }
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Seçili",
                                tint = DuoGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
