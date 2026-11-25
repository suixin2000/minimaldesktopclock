package com.digital.digitalclock.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

/**
 * Simple colon rendered as two squares (dots), no background matrix.
 * Used by Matrix theme.
 */
@Composable
fun SimpleColon(
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF00E5FF),
    inactiveColor: Color = Color(0xFF153B3B),
    lit: Boolean = true
) {
    Canvas(modifier = modifier) {
        val dotSize = minOf(size.width, size.height / 3f) * 0.8f
        val spacing = (size.height - dotSize * 2f) / 3f

        val x = (size.width - dotSize) / 2f
        val y1 = spacing
        val y2 = spacing * 2f + dotSize

        val color = if (lit) activeColor else inactiveColor

        // Top dot
        drawRect(
            color = color,
            topLeft = Offset(x, y1),
            size = Size(dotSize, dotSize)
        )

        // Bottom dot
        drawRect(
            color = color,
            topLeft = Offset(x, y2),
            size = Size(dotSize, dotSize)
        )
    }
}
