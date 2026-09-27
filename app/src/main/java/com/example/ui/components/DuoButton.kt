package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoBlueDark
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGray
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoOrangeDark
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoRedDark
import com.example.ui.theme.DuoTextDark

enum class DuoButtonColor {
    GREEN,
    BLUE,
    ORANGE,
    RED,
    OUTLINE,
    GRAY
}

@Composable
fun DuoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: DuoButtonColor = DuoButtonColor.GREEN,
    enabled: Boolean = true,
    height: Dp = 50.dp,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (topColor, bottomColor, textColor, borderColor) = when (color) {
        DuoButtonColor.GREEN -> listOf(DuoGreen, DuoGreenDark, Color.White, DuoGreenDark)
        DuoButtonColor.BLUE -> listOf(DuoBlue, DuoBlueDark, Color.White, DuoBlueDark)
        DuoButtonColor.ORANGE -> listOf(DuoOrange, DuoOrangeDark, Color.White, DuoOrangeDark)
        DuoButtonColor.RED -> listOf(DuoRed, DuoRedDark, Color.White, DuoRedDark)
        DuoButtonColor.OUTLINE -> listOf(Color.White, DuoCardBorder, DuoTextDark, DuoCardBorder)
        DuoButtonColor.GRAY -> listOf(Color(0xFFE5E5E5), Color(0xFFC4C4C4), Color(0xFFAFAFAF), Color(0xFFC4C4C4))
    }

    val actualTop = if (enabled) topColor else Color(0xFFE5E5E5)
    val actualBottom = if (enabled) bottomColor else Color(0xFFCFCFCF)
    val lipHeight = 4.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) lipHeight else 0.dp,
        label = "DuoPress"
    )

    Box(
        modifier = modifier
            .height(height + lipHeight)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bottom 3D shadow lip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = lipHeight)
                .clip(RoundedCornerShape(16.dp))
                .background(actualBottom)
        )

        // Top button face
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = pressOffset)
                .clip(RoundedCornerShape(16.dp))
                .background(actualTop)
                .then(
                    if (color == DuoButtonColor.OUTLINE) {
                        Modifier.border(2.dp, DuoCardBorder, RoundedCornerShape(16.dp))
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Composable
fun DuoTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: DuoButtonColor = DuoButtonColor.GREEN,
    enabled: Boolean = true,
    fontSize: Int = 16
) {
    DuoButton(
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled
    ) {
        val textColor = when {
            !enabled -> Color(0xFFAFAFAF)
            color == DuoButtonColor.OUTLINE -> DuoTextDark
            else -> Color.White
        }
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            letterSpacing = 0.5.sp
        )
    }
}
