package com.digital.digitalclock.themes.ledglow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.digitalclock.R
import com.digital.digitalclock.theme.RenderContext

val LedGlowFont = FontFamily(Font(R.font.dseg7_classic, FontWeight.Bold))
val LedGlowLetterFont = FontFamily(Font(R.font.dseg14_classic, FontWeight.Bold))

private val CornerShape = RoundedCornerShape(48.dp)

@Composable
fun LedGlowRender(ctx: RenderContext) {
    val scheme = ctx.colorScheme
    val width = ctx.constraintsWidth

    val active = scheme.active
    val inactive = scheme.inactive

    val timeFontSize = when (ctx.layout.id) {
        LedGlowLayouts.ID_HH_MM -> (width / 4.8f).value.sp
        LedGlowLayouts.ID_HH_MM_HALFSS -> (width / 6.5f).value.sp
        else -> (width / 6.2f).value.sp
    }
    val subSize = (width / 22f).value.sp

    val (h1, h2) = ctx.time.hourSlots(ctx.is24Hour)
    val minuteStr = ctx.time.minuteStr
    val secondStr = ctx.time.secondStr
    val colonValue = if (ctx.time.colonLit) ":" else ""

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scheme.background, CornerShape)
            .border(24.dp, scheme.border, CornerShape)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (ctx.layout.id) {
                LedGlowLayouts.ID_HH_MM -> TimeRowHhMm(
                    h1, h2, minuteStr, colonValue, timeFontSize, active, inactive
                )
                LedGlowLayouts.ID_HH_MM_HALFSS -> TimeRowHalfSs(
                    ctx, h1, h2, minuteStr, secondStr, colonValue,
                    timeFontSize, active, inactive, subSize
                )
                else -> TimeRowHhMmSs(
                    h1, h2, minuteStr, secondStr, colonValue, timeFontSize, active, inactive
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BelowRow(
                showIndicator = false,
                showDate = true,
                showWeek = true,
                ctx = ctx,
                active = active,
                inactive = inactive,
                subSize = subSize
            )
        }
    }
}

@Composable
private fun TimeRowHhMmSs(
    h1: Char, h2: Char, minuteStr: String, secondStr: String, colonValue: String,
    fs: TextUnit, active: Color, inactive: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        GlowText("8", h1.toString(), LedGlowFont, fs, active, inactive)
        GlowText("8", h2.toString(), LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText(":", colonValue, LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText("88", minuteStr, LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText(":", colonValue, LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText("88", secondStr, LedGlowFont, fs, active, inactive)
    }
}

@Composable
private fun TimeRowHhMm(
    h1: Char, h2: Char, minuteStr: String, colonValue: String,
    fs: TextUnit, active: Color, inactive: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        GlowText("8", h1.toString(), LedGlowFont, fs, active, inactive)
        GlowText("8", h2.toString(), LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText(":", colonValue, LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText("88", minuteStr, LedGlowFont, fs, active, inactive)
    }
}

@Composable
private fun TimeRowHalfSs(
    ctx: RenderContext,
    h1: Char, h2: Char, minuteStr: String, secondStr: String, colonValue: String,
    fs: TextUnit, active: Color, inactive: Color,
    subSize: TextUnit
) {
    val half = fs * 0.5f
    val indicatorSize = half * 0.225f
    val digitHeightDp = fs.value.dp * 1.35f

    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
        GlowText("8", h1.toString(), LedGlowFont, fs, active, inactive)
        GlowText("8", h2.toString(), LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText(":", colonValue, LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        GlowText("88", minuteStr, LedGlowFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.height(digitHeightDp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Indicator(ctx = ctx, active = active, inactive = inactive, size = indicatorSize)

            Row(verticalAlignment = Alignment.Bottom) {
                GlowText(":", colonValue, LedGlowFont, half, active, inactive)
                GlowText("88", secondStr, LedGlowFont, half, active, inactive)
            }
        }
    }
}

@Composable
private fun BelowRow(
    showIndicator: Boolean,
    showDate: Boolean,
    showWeek: Boolean,
    ctx: RenderContext,
    active: Color,
    inactive: Color,
    subSize: TextUnit
) {
    if (!showIndicator && !showDate && !showWeek) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (showIndicator) Indicator(ctx = ctx, active = active, inactive = inactive, size = subSize)
        if (showDate) GlowText("8888-88-88", ctx.time.dateStr, LedGlowFont, subSize, active, inactive)
        if (showWeek) GlowText("888", ctx.time.dayOfWeek, LedGlowLetterFont, subSize, active, inactive)
    }
}

@Composable
private fun Indicator(ctx: RenderContext, active: Color, inactive: Color, size: TextUnit) {
    val slots = when {
        ctx.is24Hour -> listOf('2', '4', ' ', 'H')
        ctx.time.isPm -> listOf(' ', ' ', 'P', 'M')
        else -> listOf('A', 'M', ' ', ' ')
    }
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp), verticalAlignment = Alignment.CenterVertically) {
        slots.forEach { ch ->
            Box {
                Text(text = "8", fontFamily = LedGlowLetterFont, fontSize = size, color = inactive)
                if (ch != ' ') {
                    Text(text = ch.toString(), fontFamily = LedGlowLetterFont, fontSize = size, color = active)
                }
            }
        }
    }
}

/** Glowing text with vintage LED bloom effect. */
@Composable
private fun GlowText(
    ghost: String,
    value: String,
    font: FontFamily,
    fontSize: TextUnit,
    active: Color,
    inactive: Color
) {
    Box(contentAlignment = Alignment.Center) {
        // Ghost segment
        Text(text = ghost, fontFamily = font, fontSize = fontSize, color = inactive)
        // Glow bloom behind active text
        if (value.isNotEmpty()) {
            Text(
                text = value,
                fontFamily = font,
                fontSize = fontSize,
                color = active.copy(alpha = 0.5f),
                modifier = Modifier.blur(8.dp)
            )
            // Crisp foreground text
            Text(text = value, fontFamily = font, fontSize = fontSize, color = active)
        }
    }
}
