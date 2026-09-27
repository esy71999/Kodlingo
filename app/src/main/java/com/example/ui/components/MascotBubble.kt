package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoTextDark

@Composable
fun MascotBubble(
    message: String,
    modifier: Modifier = Modifier,
    subMessage: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mascot Avatar
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, DuoGreen, CircleShape)
                .background(Color(0xFFE8FCD0)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.mascot_byte_happy_1790508489012),
                contentDescription = "Maskot Byte",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Speech bubble
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(2.dp, DuoCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = message,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DuoTextDark,
                    lineHeight = 18.sp
                )
                if (subMessage != null) {
                    Text(
                        text = subMessage,
                        fontSize = 12.sp,
                        color = Color(0xFF777777),
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
