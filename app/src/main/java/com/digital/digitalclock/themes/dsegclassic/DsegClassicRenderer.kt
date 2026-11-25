package com.digital.digitalclock.themes.dsegclassic

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digital.digitalclock.R
import com.digital.digitalclock.theme.RenderContext

/** DSEG Classic fonts: 7-segment for digits/date, 14-segment for letters. */
val Dseg7ClassicFont = FontFamily(Font(R.font.dseg7_classic, FontWeight.Bold))
val Dseg14ClassicFont = FontFamily(Font(R.font.dseg14_classic, FontWeight.Bold))

private val CornerShape = RoundedCornerShape(68.dp)

/**
 * Font-based renderer for the DSEG Classic theme. Draws a thick rounded border around
 * all layouts, uses the ghost-segment technique (inactive "8"/":" beneath the active
 * value), and arranges the time per the selected layout.
 */
@Composable
fun DsegClassicRender(ctx: RenderContext) {
    val scheme = ctx.colorScheme
    val width = ctx.constraintsWidth

    // Theme-specific settings.
    val transparency = (ctx.settings[DsegClassicTheme.SETTING_INACTIVE_ALPHA] ?: 0f).coerceIn(0f, 100f)
    val inactiveAlphaFactor = 1f - transparency / 100f
    val inactive = scheme.inactive.copy(alpha = scheme.inactive.alpha * inactiveAlphaFactor)
    val active = scheme.active
    val hideWeek = (ctx.settings[DsegClassicTheme.SETTING_HIDE_WEEK] ?: 0f) >= 0.5f
    val hideDate = (ctx.settings[DsegClassicTheme.SETTING_HIDE_DATE] ?: 0f) >= 0.5f

    val timeFontSize = when (ctx.layout.id) {
        DsegClassicLayouts.ID_HH_MM -> (width / 4.8f).value.sp
        DsegClassicLayouts.ID_HH_MM_HALFSS -> (width / 6.5f).value.sp
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
            .border(40.dp, scheme.border, CornerShape)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (ctx.layout.id) {
                DsegClassicLayouts.ID_HH_MM -> TimeRowHhMm(
                    h1, h2, minuteStr, colonValue, timeFontSize, active, inactive
                )
                DsegClassicLayouts.ID_HH_MM_HALFSS -> TimeRowHalfSs(
                    ctx, h1, h2, minuteStr, secondStr, colonValue,
                    timeFontSize, active, inactive, subSize
                )
                else -> TimeRowHhMmSs(
                    h1, h2, minuteStr, secondStr, colonValue, timeFontSize, active, inactive
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Below-time row. Date and week only (indicator is above the time for halfss).
            BelowRow(
                showIndicator = false,
                showDate = !hideDate,
                showWeek = !hideWeek,
                ctx = ctx,
                active = active,
                inactive = inactive,
                subSize = subSize
            )
        }
    }
}

/** hh:mm:ss - all slots the same size, center aligned. */
@Composable
private fun TimeRowHhMmSs(
    h1: Char, h2: Char, minuteStr: String, secondStr: String, colonValue: String,
    fs: TextUnit, active: Color, inactive: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        SegText("8", h1.toString(), Dseg7ClassicFont, fs, active, inactive)
        SegText("8", h2.toString(), Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText(":", colonValue, Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText("88", minuteStr, Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText(":", colonValue, Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText("88", secondStr, Dseg7ClassicFont, fs, active, inactive)
    }
}

/** hh:mm - hours and minutes only, same size, center aligned. */
@Composable
private fun TimeRowHhMm(
    h1: Char, h2: Char, minuteStr: String, colonValue: String,
    fs: TextUnit, active: Color, inactive: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        SegText("8", h1.toString(), Dseg7ClassicFont, fs, active, inactive)
        SegText("8", h2.toString(), Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText(":", colonValue, Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText("88", minuteStr, Dseg7ClassicFont, fs, active, inactive)
    }
}

/**
 * hh:mm:halfss - seconds at 50% size, bottom-aligned with the hh/mm slots;
 * the AM/PM/24H indicator is positioned directly above the seconds.
 *
 * Layout:
 *   [HH:MM]  [Indicator]
 *            [:SS]        (ss at half size, bottom-aligned)
 *   [Date] [Week]         (below)
 */
@Composable
private fun TimeRowHalfSs(
    ctx: RenderContext,
    h1: Char, h2: Char, minuteStr: String, secondStr: String, colonValue: String,
    fs: TextUnit, active: Color, inactive: Color,
    subSize: TextUnit
) {
    val half = fs * 0.5f
    // Indicator at 50% of current size (was half * 0.9f, now half * 0.45f)
    val indicatorSize = half * 0.45f

    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
        // Hours and minutes at full size
        SegText("8", h1.toString(), Dseg7ClassicFont, fs, active, inactive)
        SegText("8", h2.toString(), Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText(":", colonValue, Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))
        SegText("88", minuteStr, Dseg7ClassicFont, fs, active, inactive)
        Spacer(modifier = Modifier.width(8.dp))

        // Right column: indicator above seconds
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Indicator above the ss slots
            Indicator(ctx = ctx, active = active, inactive = inactive, size = indicatorSize)

            // Gap: about half the indicator's height
            Spacer(modifier = Modifier.height(indicatorSize.value.dp * 0.5f))

            // Seconds at half size, bottom-aligned with hh/mm
            Row(verticalAlignment = Alignment.Bottom) {
                SegText(":", colonValue, Dseg7ClassicFont, half, active, inactive)
                SegText("88", secondStr, Dseg7ClassicFont, half, active, inactive)
            }
        }
    }
}

/** Row beneath the time: optional indicator, date and week. */
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
        if (showDate) SegText("8888-88-88", ctx.time.dateStr, Dseg7ClassicFont, subSize, active, inactive)
        if (showWeek) SegText("888", ctx.time.dayOfWeek, Dseg14ClassicFont, subSize, active, inactive)
    }
}

/**
 * AM/PM / 24H indicator. Non-clickable by design: 12/24h is a global setting changed
 * only from the settings menu.
 */
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
                Text(text = "8", fontFamily = Dseg14ClassicFont, fontSize = size, color = inactive)
                if (ch != ' ') {
                    Text(text = ch.toString(), fontFamily = Dseg14ClassicFont, fontSize = size, color = active)
                }
            }
        }
    }
}

/** Ghost-segment slot: inactive ghost text with the active value drawn on top. */
@Composable
private fun SegText(
    ghost: String,
    value: String,
    font: FontFamily,
    fontSize: TextUnit,
    active: Color,
    inactive: Color
) {
    Box {
        Text(text = ghost, fontFamily = font, fontSize = fontSize, color = inactive)
        if (value.isNotEmpty()) {
            Text(text = value, fontFamily = font, fontSize = fontSize, color = active)
        }
    }
}
