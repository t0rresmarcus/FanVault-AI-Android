package com.fanvault.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val Purple = Color(0xFF6D28D9)
val Navy = Color(0xFF1E2A5A)
val SkyTop = Color(0xFFDCEBFA)
val SkyBottom = Color(0xFFEAF4FD)
val UserBubble = Color(0xFFEDEBF3)
val GreenOnline = Color(0xFF16A34A)

private val Colors = lightColorScheme(
    primary = Purple,
    onPrimary = Color.White,
    surface = Color.White,
    background = SkyBottom,
    onSurface = Color(0xFF1F2937)
)

@Composable
fun FanVaultTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Colors,
        shapes = Shapes(medium = RoundedCornerShape(16.dp)),
        content = content
    )
}
