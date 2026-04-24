package eu.kanade.presentation.more

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogoHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1117),
                        Color(0xFF161B22),
                        MaterialTheme.colorScheme.surface,
                    ),
                ),
            )
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // "KOMA" title with shimmer effect
        Text(
            text = "KOMA",
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 6.sp,
            style = MaterialTheme.typography.headlineLarge.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE0E0E0),
                        Color(0xFFFFFFFF),
                        Color(0xFF90CAF9),
                        Color(0xFFFFFFFF),
                        Color(0xFFE0E0E0),
                    ),
                    start = Offset(shimmerOffset, 0f),
                    end = Offset(shimmerOffset + 500f, 0f),
                ),
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        // "コマ" Japanese
        Text(
            text = "コマ",
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 8.sp,
            color = Color(0xFF58A6FF),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Accent divider
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF58A6FF),
                            Color.Transparent,
                        ),
                    ),
                ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tagline
        Text(
            text = "Every chapter. Everywhere.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            letterSpacing = 1.5.sp,
            color = Color(0xFF8B949E),
        )
    }

    HorizontalDivider(color = Color(0xFF21262D))
}
