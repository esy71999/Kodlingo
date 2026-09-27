package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoTextDark
import com.example.ui.theme.DuoYellow
import com.example.ui.theme.DuoYellowDark

enum class NodeState {
    COMPLETED,
    CURRENT,
    LOCKED
}

@Composable
fun DuoPathNode(
    lessonTitle: String,
    lessonSubtitle: String,
    iconEmoji: String,
    state: NodeState,
    horizontalOffsetFraction: Float, // -0.4f (left) to 0.4f (right)
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            contentAlignment = when {
                horizontalOffsetFraction < -0.15f -> Alignment.CenterStart
                horizontalOffsetFraction > 0.15f -> Alignment.CenterEnd
                else -> Alignment.Center
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // "BAŞLA!" speech bubble on top of current active node
                if (state == NodeState.CURRENT) {
                    Box(
                        modifier = Modifier
                            .offset(y = (-6).dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(2.dp, DuoGreen, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "BAŞLA!",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = DuoGreen
                        )
                    }
                }

                // Main Node Circle Button
                val nodeSize = 74.dp
                val lipHeight = 7.dp

                val (topBg, bottomBg, borderColor) = when (state) {
                    NodeState.COMPLETED -> listOf(DuoYellow, DuoYellowDark, DuoYellowDark)
                    NodeState.CURRENT -> listOf(DuoGreen, DuoGreenDark, DuoGreenDark)
                    NodeState.LOCKED -> listOf(Color(0xFFE5E5E5), Color(0xFFC7C7C7), Color(0xFFC7C7C7))
                }

                Box(
                    modifier = Modifier
                        .size(nodeSize + (if (state == NodeState.CURRENT) 10.dp else 0.dp))
                        .then(if (state == NodeState.CURRENT) Modifier.scale(scaleAnim) else Modifier)
                        .testTag("node_${lessonTitle.replace(" ", "_").lowercase()}"),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Pulsing outer halo for active node
                    if (state == NodeState.CURRENT) {
                        Box(
                            modifier = Modifier
                                .size(nodeSize + 10.dp)
                                .clip(CircleShape)
                                .background(DuoGreen.copy(alpha = 0.2f))
                        )
                    }

                    // 3D bottom bevel
                    Box(
                        modifier = Modifier
                            .size(nodeSize)
                            .offset(y = lipHeight)
                            .clip(CircleShape)
                            .background(bottomBg)
                    )

                    // Node Face
                    Box(
                        modifier = Modifier
                            .size(nodeSize)
                            .clip(CircleShape)
                            .background(topBg)
                            .border(3.dp, borderColor, CircleShape)
                            .clickable(
                                enabled = state != NodeState.LOCKED,
                                onClick = onClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when (state) {
                            NodeState.COMPLETED -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = iconEmoji, fontSize = 24.sp)
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Tamamlandı",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            NodeState.CURRENT -> {
                                Text(text = iconEmoji, fontSize = 28.sp)
                            }
                            NodeState.LOCKED -> {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Kilitli",
                                    tint = Color(0xFFAFAFAF),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lessonTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (state == NodeState.LOCKED) Color(0xFFAFAFAF) else DuoTextDark
                )
            }
        }
    }
}
